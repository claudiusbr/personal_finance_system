package personalfinance.persistence

import java.sql.{Connection, DriverManager, Statement}

object PersistenceTesterHelper {
  val propertiesLoader = new personalfinance.PropertiesLoader("./src/test/testprops")
  val privateLoader = new personalfinance.PropertiesLoader("private.properties")

  private val db: String = propertiesLoader.getProperty("currentdbengine")

  private val url: String = propertiesLoader.getProperty(s"${db}Url")
  private val driver: String = propertiesLoader.getProperty(s"${db}Driver")
  private val username: String = privateLoader.getProperty(s"${db}Username")
  private val password: String = privateLoader.getProperty(s"${db}Password")

  private val dbName: String = propertiesLoader.getProperty("dbname")

  lazy val connection: Connection = try {
    Class.forName(driver)
    DriverManager.getConnection(url,username,password)
  } catch {
    case e: Throwable => throw e
  }

  def loadTestDBFromDump(dumpPath: String): Unit = {
    val statement: Statement = connection.createStatement()

    io.Source
      .fromFile(dumpPath)
      .getLines
      .toList
      .foldLeft("")(_ + _)
      .split(";") // had to include this here, otherwise MySql would complain
      .foreach { statement.execute }

  }

}
