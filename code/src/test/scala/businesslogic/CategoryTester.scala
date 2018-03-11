package businesslogic

class CategoryTester extends BehaviourTester {
  "A Category" should "have a name" in {
    val ctg: Category = Category("Test")
    ctg.name should be ("Test")
  }
}
