package personalfinance
package businesslogic

import org.scalatest.GivenWhenThen
import org.mockito.Mockito.when
import personalfinance.businesslogic.transaction.{Category, Entry, Patterns, Pattern, TransactionUnit}

/**
  * integration test between Classifier, Entry,
  * Category, TransactionUnit and Patterns
  */
class StringClassifierTester extends BehaviourTester with Mocker with GivenWhenThen {
  private val stringClassifier = new StringClassifier

  private val cat1entry1desc = "a laptop from PCWorld"
  private val cat1entry2desc = "a laptop from some store I found"
  private val cat2entry1desc = "weekly shopping from local grocer"
  private val cat2entry2desc = "monthly shopping from supermarket"
  private val cat3entry1desc = "a laptop for my friend's birthday"

  private val cat1 = Category(name = "hardware",patterns = Patterns(List(
    Pattern(cat1entry1desc.substring(0,12)),
    Pattern(cat1entry2desc.substring(0,24)),
    Pattern("a laptop")
  )))

  private val cat2 = Category(name = "groceries", patterns = Patterns(List(
    Pattern(cat2entry1desc.substring(0,15)),
    Pattern(cat2entry2desc.substring(0,16))
  )))

  private val cat3 = Category(name = "gifts", patterns = Patterns(List(
    Pattern("a laptop for my")
  )))

  private val cat1entry1 = mock[Entry]
  private val cat1entry2 = mock[Entry]
  private val cat2entry1 = mock[Entry]
  private val cat2entry2 = mock[Entry]
  private val entry3 = mock[Entry]
  private val cat3entry1 = mock[Entry]

  when(cat1entry1.description).thenReturn(cat1entry1desc)
  when(cat1entry2.description).thenReturn(cat1entry2desc)
  when(cat2entry1.description).thenReturn(cat2entry1desc)
  when(cat2entry2.description).thenReturn(cat2entry2desc)
  when(entry3.description).thenReturn("some other description")
  when(cat3entry1.description).thenReturn(cat3entry1desc)

  private val catList = List(cat1,cat2)

  private val entryList = List(cat1entry1,cat1entry2,cat2entry1,cat2entry2,entry3)

  private val tu1 = TransactionUnit(cat1,List(cat1entry1))
  private val tu2 = TransactionUnit(cat1,List(cat1entry2))
  private val tu3 = TransactionUnit(cat2,List(cat2entry1))
  private val tu4 = TransactionUnit(cat2,List(cat2entry2))
  private val tu5 =  TransactionUnit(cat3,List(cat3entry1))

  "A Classifier" should "assign categories to entries when it can find them" in {
    val expectedResult = (List(tu1,tu2,tu3,tu4),List(entry3))
    stringClassifier.classify(catList,entryList) should be (expectedResult)
  }

  it should "return return all entries when it cannot find them" in {
    val listE3 = List(entry3,entry3,entry3)
    val expectedResult = (List(),List(entry3,entry3,entry3))
    stringClassifier.classify(catList,listE3) should be (expectedResult)
  }

  it should "match against longest matching prefix" in {
    Given("an entry with a prefix which matches more than one pattern")
    val listCat3Entry1 = List(cat3entry1)

    When("it is matched against all categories with which it could match")
    val cat3List = List(cat1,cat3)

    Then("it should match against the one with longest matching prefix")
    stringClassifier.classify(cat3List,listCat3Entry1) should be ((List(tu5),List()))
  }
}
