package personalfinance
package persistence

import java.sql.Connection

import personalfinance.persistence.connections._

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
    connection = DBConnector .connect(url,driver,username,password)

  def closeConnection(): Unit = connection.close()

  def isConnected: Boolean = !connection.isClosed

  def getAllCategories: String = sqlDialect.queryForAllCategories
}
