package businesslogic.transaction

import businesslogic.{BehaviourTester, Mocker}

class CategoryTester extends BehaviourTester with Mocker {

  val ctg: Category = new Category("Test")

  "A Category" should "have a name" in {
    ctg.name should be ("Test")
  }

  it should "have an empty list of Entries" in {
    ctg.entries should be (List[Entry]())
  }

  it should "be allowed to have no patterns" in {
    ctg.patterns.list shouldBe empty
  }
}
