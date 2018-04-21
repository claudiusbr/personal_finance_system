package personalfinance
package persistence

import java.sql.ResultSet

class PersistenceBridgeTest extends BehaviourTester {
  val propertiesLoader = new personalfinance.PropertiesLoader("./src/test/testprops")
  val privateLoader = new personalfinance.PropertiesLoader("private.properties")
  val persistenceBridge = new PersistenceBridge(propertiesLoader,privateLoader)

  persistenceBridge.connect()

  private def iterateResultSet[A](rs: ResultSet, op: (ResultSet,A) => A, acc: A): A = {
    if (!rs.next()) acc
    else iterateResultSet(rs, op, op(rs,acc))
  }

  private def returnAsString(rs: ResultSet, str: String): String =
    str + (for (i <- 1 to rs.getMetaData.getColumnCount)
      yield rs.getString(i))
      .mkString(" ")

  private def stringFromResultSet(rs: ResultSet): String =
    iterateResultSet[String](rs, returnAsString, "")

  "PersistenceBridge with MySql" should "connect to a database" in {
    persistenceBridge.isConnected should be (true)
  }

  it should "retrieve all categories when requested" in {
    val hardcoded = "2 hardware 1 new laptop 2" +
      "2 hardware 2 laptop from some store 2" +
      "3 groceries 3 weekly shopping 3" +
      "3 groceries 4 monthly shopping 3" +
      "1 Bank null null null"

     stringFromResultSet(
       persistenceBridge.getAllCategoriesAndPatterns) should be (hardcoded)
  }

  it should "return a single category when requested" in {
    val hardcoded = "1 Bank"
    stringFromResultSet(
      persistenceBridge.getCategory("Bank")) should be (hardcoded)
  }

}
