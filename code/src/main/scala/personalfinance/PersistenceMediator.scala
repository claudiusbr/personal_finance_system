package personalfinance

import java.sql.{ResultSet, SQLException}

import businesslogic._
import transaction._
import dates._
import org.joda.time.DateTime
import persistence.PersistenceBridge

/**
  * This object is responsible for handling the interaction between the
  * application logic and the persistence layer.
  */
object PersistenceMediator extends Mediator {
  private val persistenceBridge =
    new PersistenceBridge(propertiesLoader,privateLoader)

  def startup(): Unit = persistenceBridge.connect()

  def quit(): Unit = persistenceBridge.closeConnection()

  def getOrMakeCategory(name: String): Category = {
    val rs: ResultSet =  persistenceBridge.getCategory(name)

    // Assuming a schema `idcategory,name`, and that RS
    // can be read from left to right, as indicated here
    // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
    if (!rs.next()) {
      val newRs: ResultSet = persistenceBridge.createAndReturnCategory(name)
      if (newRs.next()) getExistingCategory(newRs)
      else throw new RuntimeException(s"tried to make category $name but " +
        "something went terribly wrong. Check Database or Stacktrace")
    } else {
      getExistingCategory(rs)
    }
  }

  def createPattern(categoryId: Int, patternValue: String): Boolean =
    persistenceBridge.createPattern(categoryId,patternValue)

  def getExistingCategory(rs: ResultSet): Category = {
    val categoryName = rs.getString(2)
    val categoryId = rs.getInt(1)
    rs.close()
    val categoryPatterns: ResultSet = persistenceBridge.getCategoryPatterns(categoryId)

    // assuming a schema `idpattern,value,category_id`
    val pats: Patterns = Patterns(
      iterateResultSet[List[Pattern]](
        categoryPatterns,
        (res, patList) => {
          Pattern(res.getString(2), Some(res.getInt(1))) +: patList
        },
        List[Pattern]())
        .reverse)

    categoryPatterns.close()

    Category(categoryName, patterns = pats, id = Some(categoryId))
  }

  def commitTransactionToDB(cats: Seq[Category]): Unit = {
    val entriesToCommit: Seq[(Double,Int,Int,Int)] = cats.flatMap {
      cat => {
        cat match {
          case Category(n, es, ps, None) => {
            val newCatID = getExistingCategory(
              persistenceBridge.createAndReturnCategory(n)).id

            ps.list.foreach {
              case Pattern(v, None) =>
                persistenceBridge.createPattern(newCatID.get, v)

              case _ => throw new RuntimeException("this should never happen." +
                "just suppressing warnings")
            }

            entriesFromCategory(Category(n, es, ps, newCatID))
          }

          case Category(_, _, ps, Some(id)) => {
            ps.list.foreach {
              case Pattern(_,Some(_)) =>

              case Pattern(v, None) =>
                persistenceBridge.createPattern(id, v)
            }

            entriesFromCategory(cat)
          }

          case _ => throw new RuntimeException("this should never happen." +
            "just suppressing warnings")
        }
      }
    }

    // TODO: communicate with user: did it work? did it fail?
    persistenceBridge.createEntrySet(entriesToCommit)
  }

  def getAllCategoriesAndPatterns(): Seq[Category] = {
    val (categories: Set[Category], patternsAndCatIds: Map[Int,Seq[Pattern]]) =
      iterateResultSet[(Set[Category], Map[Int,Seq[Pattern]])](
        rs = persistenceBridge.getAllCategoriesAndPatterns,
        op = (res, catAndPat) => {
          (catAndPat._1 + Category(res.getString(2), id = Some(res.getInt(1))), {
            val patternValue: String = res.getString(4)
            if (patternValue == null || patternValue.isEmpty) catAndPat._2
            else catAndPat._2 + (
              res.getInt(1) -> (
                Pattern(patternValue, Some(res.getInt(3)))
                  +: catAndPat._2.getOrElse(res.getInt(1),Seq[Pattern]())))
          })
        },
        acc = (Set[Category](), Map[Int,Seq[Pattern]]()))

    categories.map {
      case Category(n,e,_,Some(id)) =>
        Category(n,e,Patterns(patternsAndCatIds
            .getOrElse(id,Seq[Pattern]())
          .toList),Some(id))
      case _ => throw new RuntimeException(s"Category does not have an ID")
    }.toSeq
  }

  private def entriesFromCategory(cat: Category): Seq[(Double,Int,Int,Int)] = {
    cat.entries.map {
      case Entry(amt, dateRegistry, desc, None) => {
        val created = dateRegistry.dateCreated
        val recorded =dateRegistry.dateRecorded
        val _ = persistenceBridge.createEntryDescription(Seq((created,recorded, desc)))
        val entryDescriptionId = {
          val rs: ResultSet = persistenceBridge.getEntryDescription(desc)
          val _ = rs.next()
          rs.getInt(1)
        }

        (amt.total, cat.id.get, entryDescriptionId, amt.currency.id)
      }

      case _ => throw new RuntimeException("this should never happen." +
        " just suppressing warnings")
    }
  }

  private def iterateResultSet[A](rs: ResultSet, op: (ResultSet,A) => A, acc: A): A = {
    if (!rs.next()) acc
    else iterateResultSet(rs, op, op(rs,acc))
  }
}
