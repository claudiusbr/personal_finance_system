package businesslogic

/**
  * Integration test for Entry, Amount and Currency
  */
class EntryTester extends BehaviourTester with Mocker {

  val amtUK: Amount = Amount(20.00,GBP)
  val amtEU: Amount = Amount(80.00,EUR)

  "an Entry" should "have an Amount" in {
    val e: Entry = Entry(amtUK)
    e.amount should be (amtUK)
    val n: Entry = Entry(amtEU)
    n.amount should be (amtEU)
  }

  it should "have a constructor which allows it to be created with value only" in {
    val e: Entry = Entry(20.00)
    e.amount should be (amtUK)
  }
}
