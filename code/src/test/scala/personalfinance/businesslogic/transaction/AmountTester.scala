package personalfinance
package businesslogic
package transaction

/**
  * Integration test between amount and Currency
  */
class AmountTester extends BehaviourTester {
  val amtUK: Amount = Amount(20.00)

  "an Amount instance" should "should default to GBP if"+
    " no currency is provided" in {
    amtUK.currency should be (Currency("GBP",1))
  }
}
