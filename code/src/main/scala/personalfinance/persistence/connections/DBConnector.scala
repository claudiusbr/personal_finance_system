package personalfinance
package persistence
package connections

import java.sql.{Connection, DriverManager}

private[persistence] object DBConnector {
  def connect(url: String, driver: String, username: String,
    password: String): Connection = new DBConnector(
      url,driver,username,password)
        .connect()
}

private class DBConnector(url: String, driver: String,
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
