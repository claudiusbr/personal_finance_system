package personalfinance
package businesslogic
package transaction

class CategoryTester extends BehaviourTester with Mocker {

  val ctg: Category = Category("Test")

  "A Category" should "have a name" in {
    ctg.name should be ("Test")
    Category("a").name should be ("a")
  }

  it should "have an empty list of Entries" in {
    ctg.entries should be (List[Entry]())
  }

  it should "be allowed to have no patterns" in {
    ctg.patterns.list shouldBe empty
  }

  it should "fail if no name is passed" in {
    intercept[IllegalArgumentException] {
      Category("")
    }
  }
}
