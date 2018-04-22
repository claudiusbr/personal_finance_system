package personalfinance
package businesslogic
package transaction

import personalfinance.businesslogic.transaction.dates.{DateRegistry, DateRegistryFactory}

/**
  * Integration test for Entry, Amount, DateRegistry and Currency
  */
class EntryTester extends BehaviourTester with Mocker {

  val dateRegistryFactory: DateRegistryFactory = new DateRegistryFactory
  val dt: DateRegistry = dateRegistryFactory.getDateRegistry("2018/01/03")

  val amtUK: Amount = Amount(20.00,GBP)
  val amtEU: Amount = Amount(80.00,EUR)

  val e: Entry = Entry(amtUK,dt,"TestE")

  "an Entry" should "have an Amount" in {
    e.amount should be (amtUK)
    val n: Entry = Entry(amtEU,dt,"TestN")
    n.amount should be (amtEU)
  }

  it should "have a constructor which allows it to be created with value, dates and description only" in {
    val e: Entry = Entry(Amount(20.00),dt,"TestE")
    e.amount should be (amtUK)
  }

  it should "have a date the entry was created and a date it was recorded" in {
    e.dateCreated should be (dt.dateCreated)
    e.dateRecorded should be (dt.dateRecorded)
  }
}
