package personalfinance
package persistence

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.sql.{Date => JDate}

import org.joda.time.DateTime
import personalfinance.persistence.connections._

/**
  * this class is responsible for translating the requests of external classes into
  * the right query dialect of the databases running in the persistence layer.
  * The specific database flavour is chosen by entries in the `config.properties` and
  * `private.properties` files, placed in the root directory from which the
  * application is running
  * @param propertiesLoader a PropertiesLoader instance containing a reference to
  *                         `config.properties`
  * @param privateLoader a PropertiesLoader instance containing a reference to
  *                      `private.properties`
  */
class PersistenceBridge(propertiesLoader: PropertiesLoader, privateLoader: PropertiesLoader) {
  private var connection: Connection = _

  private val db: String = propertiesLoader.getProperty("currentdbengine")

  private val url: String = propertiesLoader.getProperty(s"${db}Url")
  private val driver: String = propertiesLoader.getProperty(s"${db}Driver")
  private val username: String = privateLoader.getProperty(s"${db}Username")
  private val password: String = privateLoader.getProperty(s"${db}Password")

  private val dbName: String = propertiesLoader.getProperty("dbname")

  private val sqlDialect: ConnectionType =
    Class.forName(s"personalfinance.persistence.connections.$db")
      .getConstructor(Class.forName("java.lang.String")).newInstance(dbName)
        .asInstanceOf[ConnectionType]

  def connect(): Unit =
    connection = DBConnector.connect(url, driver, username, password)

  def closeConnection(): Unit = connection.close()

  def isConnected: Boolean = if (connection != null) !connection.isClosed else false

  def getAllCategoriesAndPatterns: ResultSet =
    runQuery(sqlDialect.queryForAllCategoriesAndPatterns)

  def getCategory(name: String): ResultSet =
    runQuery(sqlDialect.queryForACategory(name))

  /**
    * retrieve all patterns for a category found in the database
    * based on the category's id
    * @param categoryId the id of the category for which a pattern is needed
    * @return the ResultSet of the query
    */
  def getCategoryPatterns(categoryId: Int): ResultSet =
    runQuery(sqlDialect.queryForCategoryPatterns(categoryId))

  /**
    * Creates a category and immediately retrieves its ID in the database
    * @param name the name of the new category
    * @return a ResultSet with the Category from the database
    */
  def createAndReturnCategory(name: String): ResultSet = {
    val statement = sqlDialect.createCategoryOnly(name)
    if (executeUpdate(statement) > 0) {
      getCategory(name)
    } else {
      throw new Exception(s"statement $statement did not execute any updates")
    }
  }

  /**
    * Tries to create a category and returns true if it can create,
    * and false if cannot. Exceptions should be handled the caller
    * @param name the name of the category to be created
    * @return true if the category has been created, false if otherwise.
    */
  def createCategory(name: String): Boolean = {
    val statement = sqlDialect.createCategoryOnly(name)
    executeUpdate(statement) > 0
  }

  /**
    * Tries to create a pattern
    * @param categoryId the id of the category attached to this pattern
    * @param patternValue the value of the pattern
    * @return true if the pattern was created, false otherwise
    */
  def createPattern(categoryId: Int, patternValue: String): Boolean =
    executeUpdate(sqlDialect.createPatternOnly(categoryId,patternValue)) > 0


  def createEntryDescription(entryDescriptions: Seq[(DateTime, DateTime, String)]): Boolean = {

    val result: Boolean = executePS(sqlDialect.createEntryDescriptionPS,
      (st: PreparedStatement) => {
      val updateResult: Seq[Boolean] = {
        entryDescriptions.map {
          case (created: DateTime, recorded: DateTime, description: String) =>
            st.setDate(1, new JDate(created.getMillis))
            st.setDate(2, new JDate(recorded.getMillis))
            st.setString(3, description)
            st.executeUpdate() > 0
        }
      }
      connection.commit()
      updateResult.reduce( _ && _)
    })

    result
  }

  def getEntryDescription(description: String): ResultSet =
    runQuery(sqlDialect.getEntryDescription(description))


  /**
    * this is the class for commiting entries to the database.
    * This instance allows for two entries to be commited.
    * @param entries a seq of Tuple5, each with the following:
    *          date created: DateTime,
    *          date recorded: DateTime,
    *          entry amount: Double,
    *          category Id: Int,
    *          entry description Id: Int
    * @return true if the update was successful, otherwise false
    */
  def createEntrySet(entries: Seq[(Double,Int,Int, Int)]): Boolean = {
    if (entries.foldLeft(0.0)((sum,entry) => sum + entry._1) != 0)
      throw new RuntimeException(
        "Sum of entries did not equal 0. Transaction not commited")
    val result = executePS(sqlDialect.createEntryPS,(st: PreparedStatement) => {
      val updateResult: Seq[Boolean] = entries.map {
        case (amount: Double, catId: Int, descriptionId: Int, currencyId: Int) =>
          st.setDouble(1, amount)
          st.setInt(2, catId)
          st.setInt(3, descriptionId)
          st.setInt(4, currencyId)
          st.executeUpdate() > 0
      }

      connection.commit()

      updateResult.reduce(_ && _)
    })

    result
  }


  //TODO: add functionality to allow for more than two entries to be commited at a time
  /**
    * this is the class for commiting entries to the database.
    * This instance allows for two entries to be commited.
    * @param amountDebit the amount for the debit side -- normally positive
    * @param amountCredit the amount for the debit side -- normally negative
    * @param categoryIdDebit the category to be debited
    * @param categoryIdCredit the category to be credited
    * @param entryDescriptionId the description of the entry
    * @param currencyId the id of the currency
    * @return true if the update was successful, otherwise false
    */
  def createEntrySet(amountDebit: Double, amountCredit: Double,
                     categoryIdDebit: Int, categoryIdCredit: Int,
                     entryDescriptionId: Int, currencyId: Int): Boolean = {

    if (amountCredit + amountDebit != 0) throw new IllegalArgumentException("")

    val result: Boolean = executePS(sqlDialect.createEntryPS,
      (st: PreparedStatement) => {

      // for readability
      val stDebit = st
      val stCredit = st

      stDebit.setDouble(1, amountDebit)
      stDebit.setInt(2, categoryIdDebit)
      stDebit.setInt(3, entryDescriptionId)
      stDebit.setInt(4, currencyId)
      val resultDebit: Boolean = stDebit.executeUpdate() > 0

      stCredit.setDouble(1,amountCredit)
      stCredit.setInt(2,categoryIdCredit)
      stCredit.setInt(3,entryDescriptionId)
      stCredit.setInt(4,currencyId)
      val resultCredit: Boolean = stCredit.executeUpdate() > 0

      connection.commit()

      resultDebit && resultCredit
    })

    result
  }

  def getSummary(from: DateTime, to: DateTime): ResultSet = {
    executePS[ResultSet](sqlDialect.getSummaryPS(),
      (st: PreparedStatement) => {
        st.setDate(1,new JDate(from.getMillis))
        st.setDate(2,new JDate(to.getMillis))
        st.executeQuery()
      }, leaveOpen = true)
  }

  private def executeUpdate(statement: String): Int = {
    val st = connection.createStatement
    val result: Int = try {
      st.executeUpdate(statement)
    } catch {
      case e: Throwable => throw e
    }
    result
  }

  private def executePS[T](statement: String, op: PreparedStatement => T, leaveOpen: Boolean = false): T = {

    connection setAutoCommit false
    val st = connection prepareStatement statement
    try op(st) catch {
      case e: SQLException => {
        e.printStackTrace()
        if (connection != null) try {
          System.err.print("Attempting to roll back")
          connection.rollback()
        } catch {
          case otherE: SQLException => {
            otherE.printStackTrace()
            throw otherE
          }
        }
        throw e
      }
    } finally {
      connection setAutoCommit true
      if (st != null && !leaveOpen) st.close()
    }
  }

  private def runQuery(query: String): ResultSet = {
    val st = connection.createStatement
    val rs: ResultSet = try {
      st.executeQuery(query)
    } catch {
      // TODO: handle this better: pass it to a logger
      case e: SQLException => {
        if (st != null) st close()
        throw e
      }
    }
    rs
  }

}
