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

  private val testContents = List(
    "date,description,amount",
    "2018-01-03,blabla,200.00",
    "2018-01-03,bleble,-10.00"
  )

  private val mockPL = mock[PropertiesLoader]
  when(mockPL.getProperty("date")).thenReturn("date")
  when(mockPL.getProperty("description")).thenReturn("description")
  when(mockPL.getProperty("amount")).thenReturn("amount")

  private val ep = new EntryParser(mockDateRegistryFactory)

  "an EntryParser" should "parse file contents into Entry instances" in{
    ep.parseCSVLines(testContents,mockPL) should be (List(
      Entry(200.00,mockDateRegistry,"blabla"),
      Entry(-10.00,mockDateRegistry,"bleble")))
  }

}
