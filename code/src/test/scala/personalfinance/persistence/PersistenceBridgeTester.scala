package personalfinance
package persistence

import java.sql.{ResultSet, SQLException}
import java.lang.{Integer => JInteger}

import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import personalfinance.businesslogic.transaction.dates.DateRegistryFactory

class PersistenceBridgeTester extends BehaviourTester with BeforeAndAfter with BeforeAndAfterAll {

  private val propertiesLoader =
    new personalfinance.PropertiesLoader("./src/test/testprops")
  private val privateLoader =
    new personalfinance.PropertiesLoader("private.properties")

  private val persistenceBridge =
    new PersistenceBridge(propertiesLoader,privateLoader)

  private val helper = new PersistenceTesterHelper(propertiesLoader,privateLoader)

  private val sampleDR = (new DateRegistryFactory).getDateRegistry("2018/04/21")

  private val entryDescription: Seq[(DateTime,DateTime,String)] =
    Seq((sampleDR.dateCreated,sampleDR.dateRecorded,"test entry description"))

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
    rs.getInt(1) shouldBe a [JInteger]
    rs.getString(2) should be ("Test2")
  }

  it should "create a pattern and return true if it works" in {
    persistenceBridge.createPattern(1,"test pattern") should be (true)
  }

  it should "create entry descriptions" in {
    persistenceBridge
      .createEntryDescription(
        entryDescription) should be (true)
  }

  it should "retrieve entry descriptions details" in {
    val rs: ResultSet = persistenceBridge
      .getEntryDescription(entryDescription.head._3)

    val _ = rs.next()
    rs.getInt(1) shouldBe a [JInteger]
    rs.getString(2) should be (entryDescription.head._3)
  }

  it should "add entries to the database when requested" in {
    val date = new DateTime(1514764800000L)
    persistenceBridge
      .createEntrySet(10.00,-10.00,1,3,1,1) should be (true)

    persistenceBridge
      .createEntrySet(
        Seq((10.0,1,3,1),(-10.0,2,3,1))) should be (true)
  }

  it should "fail when the entries do not add up to zero" in {
    val date = new DateTime(1514764800000L)

    intercept [IllegalArgumentException] {
      persistenceBridge
        .createEntrySet(10.01, -10.00, 1, 3, 1,1)
    }

    intercept [RuntimeException] {
      persistenceBridge
        .createEntrySet(Seq((10.0,1,3,1),(-10.0,2,3,1), (-10.0,2,3,1)))
    }
  }

  // run this test last
  it should "close the connection once it is done" in {
    persistenceBridge.closeConnection()
    persistenceBridge.isConnected should be (false)
  }
}
