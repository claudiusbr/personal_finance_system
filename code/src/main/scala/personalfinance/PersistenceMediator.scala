package personalfinance

import java.sql.{ResultSet, SQLException}

import businesslogic._
import transaction._
import dates._
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

  def getCategory(name: String): Category = {
    val rs: ResultSet =  persistenceBridge.getCategory(name)

    // Assuming a schema `idcategory,name`, and that RS
    // can be read from left to right, as indicated here
    // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
    if (rs.next()) {
      val categoryName = rs.getString(2)
      val categoryId = rs.getInt(1)
      rs.close()
      val categoryPatterns: ResultSet = persistenceBridge.getCategoryPatterns(categoryId)

      // assuming a schema `idpattern,value,category_id`
      val patterns: Patterns = Patterns(
        iterateResultSet[List[String]](
          categoryPatterns,
          (res, patList) => {
            res.getString(2) +: patList
          },
          List[String]())
          .reverse)

      categoryPatterns.close()

      new Category(categoryName, patterns, categoryId)
    } else {
      throw new SQLException(s"The query for category $name did not return any results.")
    }
  }

  private def iterateResultSet[A](rs: ResultSet, op: (ResultSet,A) => A, acc: A): A = {
    if (!rs.next()) acc
    else iterateResultSet(rs, op, op(rs,acc))
  }
}
