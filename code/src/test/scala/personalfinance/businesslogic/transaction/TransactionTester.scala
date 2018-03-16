package personalfinance
package businesslogic
package transaction

import org.mockito.Mockito.when

import validation._

class TransactionTester extends BehaviourTester with Mocker {
  protected val mockAmount: Amount = mock[Amount]
  when(mockAmount.total).thenReturn(10.0d)

  protected val mockEntry: Entry = mock[Entry]
  when(mockEntry.amount).thenReturn(mockAmount)

  protected val mockOtherAmount: Amount = mock[Amount]
  when(mockOtherAmount.total).thenReturn(-10.0d)

  protected val mockOtherEntry: Entry = mock[Entry]
  when(mockOtherEntry.amount).thenReturn(mockOtherAmount)

  protected val trans: Transaction = new Transaction

  protected val laptop: Category = new Category("Laptop")
  protected val bank: Category = new Category("Bank")

  protected val tuMatch: TransactionUnit = TransactionUnit(laptop, List(mockEntry, mockOtherEntry))
  protected val tuAlsoMatch: TransactionUnit = TransactionUnit(bank, List(mockOtherEntry, mockEntry))

  it should "execute the transaction on a single TransactionUnit" in {
    val result = trans.execute(List(tuMatch))

    result.head.name should be("Laptop")
    result.head.entries should be(List(mockEntry, mockOtherEntry))
  }

  it should "execute the transaction on multiple transaction units" in {
    val result = trans.execute(List(tuMatch,tuAlsoMatch))

    result.head.name should be("Laptop")
    result.head.entries should be(List(mockEntry, mockOtherEntry))

    result.tail.head.name should be ("Bank")
    result.tail.head.entries should be (List(mockOtherEntry,mockEntry))
  }

}

class TransactionEntriesValidatorTester extends TransactionTester with BehaviourTester with Mocker {

  private val tev: TransactionEntriesValidator = new TransactionEntriesValidator

  private val tuFail: TransactionUnit = TransactionUnit(laptop,List(mockEntry,mockEntry))

  "a TransactionEntriesValidator" should "return Fail if no list of TransactionUnits is given" in {
    val lse = List(mockEntry, mockEntry)
    tev.validate(lse) should be(Fail("A list of TransactionUnits is required", lse))
  }

  it should "validate entries to ensure they equal zero" in {
    tev.validate(List(tuMatch)) shouldBe a [Pass]
    tev.validate(List(tuFail)) shouldBe a [Fail]
  }
}

class TransactionUnitValidatorTester extends TransactionTester with BehaviourTester with Mocker {

  private val tcv: TransactionUnitValidator = new TransactionUnitValidator

  "a TransactionCategoriesValidator" should "check that there is at least one category" in {
    tcv.validate(List(tuMatch)) shouldBe a [Pass]
    tcv.validate(List[TransactionUnit]()) shouldBe a [Fail]
  }
}

