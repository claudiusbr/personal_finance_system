package personalfinance
package persistence

import java.sql.Connection

import personalfinance.persistence.connections._

object PersistenceMediator {
  private val propertiesLoader = new PropertiesLoader("config.properties")
  private val privateLoader = new PropertiesLoader("private.properties")

  private val db = propertiesLoader.getProperty("currentdb")

  private val url = propertiesLoader.getProperty(s"${db}Url")
  private val driver = propertiesLoader.getProperty(s"${db}Driver")
  private val username = privateLoader.getProperty(s"${db}Username")
  private val password = privateLoader.getProperty(s"${db}Password")

  private val sqlDialect: ConnectionType =
    Class.forName(s"personalfinance.persistence.connections.$db")
      .getConstructor().newInstance().asInstanceOf[ConnectionType]

  private def connect(): Connection = DBConnector
    .connect(url,driver,username,password)

  def getAllCategories: String = sqlDialect.queryForAllCategories
}
