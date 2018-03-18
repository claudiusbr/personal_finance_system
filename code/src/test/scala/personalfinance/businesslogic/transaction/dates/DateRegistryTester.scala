package personalfinance
package businesslogic
package transaction
package dates

import org.joda.time.DateTime

/**
  * Integration test between DateRegistry and DateTime
  */
class DateRegistryTester extends BehaviourTester {
  "A DateRegistry" should "be able to be instantiated with only one argument" in {
    val now = DateTime.now()
    val dt = new DateRegistry(now)
    dt.dateCreated should be (now)
  }
}
