package businesslogic
package transaction

import org.joda.time.DateTime

/**
  * Integration test for Entry, Amount, DateRegistry and Currency
  */
class EntryTester extends BehaviourTester with Mocker {

  val dt: DateRegistry = new DateRegistry(DateTime.now,DateTime.now)

  val amtUK: Amount = Amount(20.00,GBP)
  val amtEU: Amount = Amount(80.00,EUR)

  "an Entry" should "have an Amount" in {
    val e: Entry = Entry(amtUK,dt,"TestE")
    e.amount should be (amtUK)
    val n: Entry = Entry(amtEU,dt,"TestN")
    n.amount should be (amtEU)
  }

  it should "have a constructor which allows it to be created with value, dates and description only" in {
    val e: Entry = Entry(20.00,dt,"TestE")
    e.amount should be (amtUK)
  }
}
