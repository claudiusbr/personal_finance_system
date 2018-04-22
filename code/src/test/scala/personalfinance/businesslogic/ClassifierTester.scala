package personalfinance
package businesslogic

import org.mockito.Mockito.when
import personalfinance.businesslogic.transaction.{Category, Entry, Patterns, Pattern, TransactionUnit}

/**
  * integration test between Classifier, Entry,
  * Category, TransactionUnit and Patterns
  */
class ClassifierTester extends BehaviourTester with Mocker {
  private val classifier = new Classifier

  private val cat1entry1desc = "a new laptop from PCWorld"
  private val cat1entry2desc = "a laptop from some store I found"
  private val cat2entry1desc = "weekly shopping from local grocer"
  private val cat2entry2desc = "monthly shopping from supermarket"

  private val cat1 = Category(name = "hardware",patterns = Patterns(List(
    Pattern(cat1entry1desc.substring(2,12)),
    Pattern(cat1entry2desc.substring(2,24))
  )))

  private val cat2 = Category(name = "groceries", patterns = Patterns(List(
    Pattern(cat2entry1desc.substring(0,15)),
    Pattern(cat2entry2desc.substring(0,16))
  )))


  private val cat1entry1 = mock[Entry]
  private val cat1entry2 = mock[Entry]
  private val cat2entry1 = mock[Entry]
  private val cat2entry2 = mock[Entry]
  private val entry3 = mock[Entry]

  when(cat1entry1.description).thenReturn(cat1entry1desc)
  when(cat1entry2.description).thenReturn(cat1entry2desc)
  when(cat2entry1.description).thenReturn(cat2entry1desc)
  when(cat2entry2.description).thenReturn(cat2entry2desc)
  when(entry3.description).thenReturn("some other description")

  private val catList = List(cat1,cat2)

  private val entryList = List(cat1entry1,cat1entry2,cat2entry1,cat2entry2,entry3)

  private val tu1 = TransactionUnit(cat1,List(cat1entry1))
  private val tu2 = TransactionUnit(cat1,List(cat1entry2))
  private val tu3 = TransactionUnit(cat2,List(cat2entry1))
  private val tu4 = TransactionUnit(cat2,List(cat2entry2))

  "A Classifier" should "assign categories to entries when it can find them" in {
    val expectedResult = (List(tu1,tu2,tu3,tu4),List(entry3))
    classifier.classify(catList,entryList) should be (expectedResult)
  }

  it should "return return all entries when it cannot find them" in {
    val listE3 = List(entry3,entry3,entry3)
    val expectedResult = (List(),List(entry3,entry3,entry3))
    classifier.classify(catList,listE3) should be (expectedResult)
  }
}
