package personalfinance
package persistence

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}

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
