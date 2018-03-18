package personalfinance
package persistence
package connections

import java.sql.{Connection, DriverManager}

private[connections] object MysqlConnector {
  private lazy val props: PropertiesLoader =
    new PropertiesLoader("config.properties")
  private lazy val privateProps: PropertiesLoader =
    new PropertiesLoader("private.properties")

  def connect(): Connection = new MysqlConnector(
    url=props.getProperty("mysqlurl"),
    driver = props.getProperty("mysqldriver"),
    username = privateProps.getProperty("mysqlusername"),
    password = privateProps.getProperty("mysqlpassword")
  ).connect()
}


private[connections] class MysqlConnector(url: String, driver: String,
  username: String, password: String) {

  def connect(): Connection = {
    try {
      Class.forName(driver)
      DriverManager.getConnection(url,username,password)
    } catch {
      case e: Throwable => throw e
    }
  }
}
