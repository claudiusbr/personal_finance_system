package personalfinance
package businesslogic
package transaction

import scala.util.matching.Regex

class PatternsTester extends  BehaviourTester with Mocker {
  private val reg: Regex = ".*this.*is.*a.*test".r
  private val mockCategory: Category = mock[Category]
  private val pat: Patterns = new Patterns(List(reg))

  "a Pattern" should "have a regex member" in {
    pat.list should be (List(reg))
  }
}
