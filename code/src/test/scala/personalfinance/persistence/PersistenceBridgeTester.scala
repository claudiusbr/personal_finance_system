package personalfinance
package persistence

import java.sql.ResultSet

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}

class PersistenceBridgeTester extends BehaviourTester with BeforeAndAfter with BeforeAndAfterAll {

  protected val propertiesLoader =
    new personalfinance.PropertiesLoader("./src/test/testprops")
  protected val privateLoader =
    new personalfinance.PropertiesLoader("private.properties")

  protected def iterateResultSet[A](rs: ResultSet, op: (ResultSet,A) => A, acc: A): A = {
    if (!rs.next()) acc
    else iterateResultSet(rs, op, op(rs,acc))
  }

  protected def returnAsString(rs: ResultSet, str: String): String =
    str + (for (i <- 1 to rs.getMetaData.getColumnCount)
      yield rs.getString(i))
      .mkString(" ")

  protected def stringFromResultSet(rs: ResultSet): String =
    iterateResultSet[String](rs, returnAsString, "")

  private val persistenceBridge =
    new PersistenceBridge(propertiesLoader,privateLoader)

  private val helper = new PersistenceTesterHelper(propertiesLoader,privateLoader)

  private def overWriteTestDB(): Unit =
    helper.loadTestDBFromDump(propertiesLoader.getProperty("testDumpPath"))


  override def beforeAll(): Unit = overWriteTestDB()

  override def afterAll(): Unit = {
    overWriteTestDB()
    helper.connection.close()
  }

  /* overwrite any changes to test db with the latest test db dump
     see testpropsdb on how to recreate this file if the test db changed

     TODO find a better way to do this. Reloading before each test is causing performance issues

   */

  "PersistenceBridge with MySql" should "connect to a database" in {
    persistenceBridge.connect()
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

    val hardcoded2 = "2 hardware"
    stringFromResultSet(
      persistenceBridge.getCategory("hardware")) should be (hardcoded2)
  }

  it should "return nothing the single category requested does not exist" in {
    persistenceBridge.getCategory("Blorg").next() should be (false)
  }

  it should "return the patterns for a category" in {
    val patterns = "1 new laptop 22 laptop from some store 2"
    stringFromResultSet(
      persistenceBridge.getCategoryPatterns(2)) should be (patterns)
  }


  it should "create a category and return true if it works" in {
    persistenceBridge.createCategory("Test1") should be (true)
  }


  it should "create a category and return the category details" in {
    val rs = persistenceBridge.createAndReturnCategory("Test2")
    val _ = rs.next()
    rs.getInt(1) shouldBe a [java.lang.Integer]
    rs.getString(2) should be ("Test2")
  }

  it should "create a pattern and return true if it works" in {
    persistenceBridge.createPattern(1,"test pattern") should be (true)
  }

  // run this test last
  it should "close the connection once it is done" in {
    persistenceBridge.closeConnection()
    persistenceBridge.isConnected should be (false)
  }
}
