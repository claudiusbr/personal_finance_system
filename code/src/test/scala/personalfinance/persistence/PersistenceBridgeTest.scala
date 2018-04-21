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

  "PersistenceBridge with MySql" should "connect to a database" in {
    persistenceBridge.isConnected should be (true)
  }

  it should "retrieve all categories when requested" in {
    val hardcoded = "2 hardware 1 new laptop 2" +
      "2 hardware 2 laptop from some store 2" +
      "3 groceries 3 weekly shopping 3" +
      "3 groceries 4 monthly shopping 3" +
      "1 Bank null null null"

    iterateResultSet[String](
      persistenceBridge.getAllCategoriesAndPatterns,
      (rs,str) => {str + s"${rs.getString(1)} ${rs.getString(2)} ${rs.getString(3)} ${rs.getString(4)} ${rs.getString(5)}"},
      ""
    ) should be (hardcoded)
  }

}
