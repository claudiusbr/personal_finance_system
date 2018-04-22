package personalfinance
package persistence

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.sql.{Date => JDate}
import java.util.InputMismatchException

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

  def createEntryDescription(description: String): Boolean =
    executeUpdate(sqlDialect.createEntryDescription(description)) > 0

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
  def createEntrySet(entries: Seq[(DateTime,DateTime,Double,Int,Int)]): Boolean = {
    if (entries.foldLeft(0.0)((sum,entry) => sum + entry._3) != 0)
      throw new RuntimeException(
        "Sum of entries did not equal 0. Transaction not commited")

    connection.setAutoCommit(false)
    val st = connection.prepareStatement(sqlDialect.createEntryPS)
    try {
      val updateResult: Seq[Boolean] = entries.map {
        case (created: DateTime, recorded: DateTime, amount: Double, catId: Int, descriptionId: Int) =>
          st.setDate(1, new JDate(created.getMillis))
          st.setDate(2, new JDate(recorded.getMillis))
          st.setDouble(3, amount)
          st.setInt(4,catId)
          st.setInt(5,descriptionId)
          st.executeUpdate() > 0
      }

      connection.commit()

      updateResult.reduce(_ && _)

    } catch {
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
      if (st != null) st.close()
    }

  }

  /**
    * this is the class for commiting entries to the database.
    * This instance allows for two entries to be commited.
    * @param dateCreated the date created
    * @param dateRecorded the date recorded
    * @param amountDebit the amount for the debit side -- normally positive
    * @param amountCredit the amount for the debit side -- normally negative
    * @param categoryIdDebit the category to be debited
    * @param categoryIdCredit the category to be credited
    * @param entryDescriptionId the description of the entry
    * @return true if the update was successful, otherwise false
    */
  def createEntrySet(dateCreated: DateTime, dateRecorded: DateTime,
                     amountDebit: Double, amountCredit: Double,
                     categoryIdDebit: Int, categoryIdCredit: Int,
                     entryDescriptionId: Int): Boolean = {
    if (amountCredit + amountDebit != 0)
      throw new IllegalArgumentException("")

    connection.setAutoCommit(false)
    val stDebit = connection.prepareStatement(sqlDialect.createEntryPS)
    val stCredit = connection.prepareStatement(sqlDialect.createEntryPS)

    try {
      stDebit.setDate(1, new JDate(dateCreated.getMillis))
      stDebit.setDate(2, new JDate(dateRecorded.getMillis))
      stDebit.setDouble(3, amountDebit)
      stDebit.setInt(4, categoryIdDebit)
      stDebit.setInt(5, entryDescriptionId)
      stCredit.setDate(1, new JDate(dateCreated.getMillis))
      stCredit.setDate(2, new JDate(dateRecorded.getMillis))
      stCredit.setDouble(3,amountCredit)
      stCredit.setInt(4,categoryIdCredit)
      stCredit.setInt(5,entryDescriptionId)

      val resultDebit: Boolean = stDebit.executeUpdate() > 0
      val resultCredit: Boolean = stCredit.executeUpdate() > 0

      connection.commit()

      resultDebit && resultCredit

    } catch {
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
      if (stDebit != null) stDebit.close()
      if (stCredit != null) stCredit.close()
    }
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
