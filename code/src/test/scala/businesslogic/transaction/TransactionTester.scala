package businesslogic
package transaction

import org.mockito.Mockito.when

class TransactionTester extends BehaviourTester with Mocker {
  "a Transaction" should "validate entries to ensure they equal zero" in {
    val mockEntry = mock[Entry]
    val mockAmount = mock[Amount]
    when(mockEntry.amount).thenReturn(mockAmount)
    when(mockAmount.total).thenReturn(10.0d)

    val trans: Transaction = new Transaction
    trans.sumEqualsZero(List(mockEntry,mockEntry)) should be (false)

    val mockOtherEntry = mock[Entry]
    val mockOtherAmount = mock[Amount]
    when(mockOtherEntry.amount).thenReturn(mockOtherAmount)
    when(mockOtherAmount.total).thenReturn(-10.0d)

    trans.sumEqualsZero(List(mockEntry,mockOtherEntry)) should be (true)
  }
}
