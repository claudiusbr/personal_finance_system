package personalfinance
package persistence

import java.sql.{Connection, ResultSet}

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

  private val db: String = propertiesLoader.getProperty("currentdb")

  private val url: String = propertiesLoader.getProperty(s"${db}Url")
  private val driver: String = propertiesLoader.getProperty(s"${db}Driver")
  private val username: String = privateLoader.getProperty(s"${db}Username")
  private val password: String = privateLoader.getProperty(s"${db}Password")

  private val sqlDialect: ConnectionType =
    Class.forName(s"personalfinance.persistence.connections.$db")
      .getConstructor().newInstance().asInstanceOf[ConnectionType]

  def connect(): Unit =
    connection = DBConnector.connect(url, driver, username, password)

  def closeConnection(): Unit = connection.close()

  def isConnected: Boolean = !connection.isClosed

  def getAllCategories: ResultSet =
    runQuery(sqlDialect.queryForAllCategories)

  def getCategory(name: String): ResultSet =
    runQuery(sqlDialect.queryForACategory(name))

  private def runQuery(query: String): ResultSet =
    connection.createStatement.executeQuery(query)

}
