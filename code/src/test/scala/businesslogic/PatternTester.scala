package businesslogic

import scala.util.matching.Regex

class PatternTester extends  BehaviourTester with Mocker {
  private val reg: Regex = ".*this.*is.*a.*test".r
  private val mockCategory: Category = mock[Category]
  private val pat: Pattern = Pattern(reg,List(mockCategory))

  "a Pattern" should "have a regex member" in {
    pat.pattern should be (reg)
  }

  it should "have a collection of categories as a member" in {
    pat.categories.head should be (mockCategory)
  }
}
