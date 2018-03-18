package personalfinance
package businesslogic

import org.mockito.Mockito.when
import personalfinance.businesslogic.transaction.{Category, Entry, TransactionUnit}

/**
  * integration test between Classifier, Entry,
  * Category and TransactionUnit
  */
class ClassifierTester extends BehaviourTester with Mocker {
  private val classifier = new Classifier


  private val mockCat1 = mock[Category]
  private val mockCat2 = mock[Category]
  private val cat1Description1 = "a new laptop"
  private val cat1Description2 = "laptop from some store"
  private val cat2Description1 = "weekly shopping"
  private val cat2Description2= "monthly shopping"

  private val entry1 = mock[Entry]
  private val entry2 = mock[Entry]
  private val entry3 = mock[Entry]

  when(entry1.description).thenReturn(cat1Description2)
  when(entry2.description).thenReturn(cat2Description1)
  when(entry3.description).thenReturn("some other description")

  private val catMapList = List(
    Map(cat1Description1 -> mockCat1, cat1Description2 -> mockCat1),
    Map(cat2Description1 -> mockCat2, cat2Description2 -> mockCat2)
  )

  private val entryList = List(entry1,entry2,entry3)

  private val tu1 = TransactionUnit(mockCat1,List(entry1))
  private val tu2 = TransactionUnit(mockCat2,List(entry2))

  "A Classifier" should "assign categories to entries when it can find them" in {
    val expectedResult = (List(tu1,tu2),List(entry3))
    classifier.classify(catMapList,entryList) should be (expectedResult)
  }

  it should "return return all entries when it cannot find them" in {
    val listE3 = List(entry3,entry3,entry3)
    val expectedResult = (List(),List(entry3,entry3,entry3))
    classifier.classify(catMapList,listE3) should be (expectedResult)
  }
}
