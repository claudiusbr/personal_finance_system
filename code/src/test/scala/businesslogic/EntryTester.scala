package businesslogic

class EntryTester extends BehaviourTester {
  "an Entry" should "have an amount" in {
    val e: Entry = Entry(amount = 20.00)
    e.amount should be (20.00)
    val n: Entry = Entry(amount = 80.00)
    n.amount should be (80.00)
  }
}
