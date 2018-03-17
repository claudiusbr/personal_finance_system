package personalfinance
package businesslogic
package transaction

import org.mockito.Mockito.when
import personalfinance.input.PropertiesLoader

/**
  * Integration test between Entry and EntryParser
  */
class EntryParserTester extends BehaviourTester with Mocker {
  private val mockDateRegistryFactory = mock[DateRegistryFactory]
  private val mockDateRegistry = mock[DateRegistry]
  when(mockDateRegistryFactory.getDateRegistry("2018-01-03"))
    .thenReturn(mockDateRegistry)

  private val expectedResult = List(
    Entry(200.00,mockDateRegistry,"blabla"),
    Entry(-10.00,mockDateRegistry,"bleble"))

  private val testContents = List(
    "date,description,amount",
    "2018-01-03,blabla,200.00",
    "2018-01-03,bleble,-10.00"
  )

  private val diffOrder = List(
    "description,date,amount",
    "blabla,2018-01-03,200.00",
    "bleble,2018-01-03,-10.00"
  )

  private val diffHeaders = List(
    "transaction description,transaction date,credit/debit",
    "blabla,2018-01-03,200.00",
    "bleble,2018-01-03,-10.00"
  )

  private val interpolatedComma = List(
    "transaction description,transaction date,credit/debit",
    "\"blabla, and blo\",2018-01-03,200.00",
    "\"bleble,and bla\",2018-01-03,-10.00"
  )




  private val mockPL = mock[PropertiesLoader]
  when(mockPL.getProperty("date")).thenReturn("date")
  when(mockPL.getProperty("description")).thenReturn("description")
  when(mockPL.getProperty("amount")).thenReturn("amount")

  private val ep = new EntryParser(mockDateRegistryFactory)

  "an EntryParser" should "parse file contents into Entry instances" in{
    ep.parseCSVLines(testContents,mockPL) should be (expectedResult)
  }

  it should "parse the files even if the columns are not in order" in {
    ep.parseCSVLines(diffOrder,mockPL) should be (expectedResult)
  }

  it should "parse the files even with columns have different headers" in {

    when(mockPL.getProperty("date")).thenReturn("transaction date")
    when(mockPL.getProperty("description"))
      .thenReturn("transaction description")
    when(mockPL.getProperty("amount")).thenReturn("credit/debit")

    ep.parseCSVLines(diffHeaders,mockPL) should be (expectedResult)
  }

  it should "parse the contents even if they have interpolated commas" in {
    ep.parseCSVLines(interpolatedComma,mockPL) should be (List(
      Entry(200.00,mockDateRegistry,"\"blabla  and blo\""),
      Entry(-10.00,mockDateRegistry,"\"bleble and bla\"")))
    }
}