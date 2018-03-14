package businesslogic
package transaction

import java.lang.ClassCastException

import validation._

final class TransactionEntriesValidator extends Validator {

  def validate(what: List[Any]): TestResult = {
      try { transactionUnitsAreValid(what.asInstanceOf[List[TransactionUnit]]) }
      catch {
        case _: ClassCastException => Fail("A list of TransactionUnits is required",what)
        case e: Throwable => throw e
      }
  }

  private def transactionUnitsAreValid(tu: List[TransactionUnit]): TestResult =
    sumOfEntriesEqualZero(tu.flatMap(_.entries))

  /**
    * this method takes a list of entries and adds them up to make
    * sure they equal zero, thus preserving double entry
    * @param entries a list of entries
    * @return true if the sum of the amounts of all entries equals
    *         zero
    */
  private def sumOfEntriesEqualZero(entries: List[Entry]): TestResult = {
    val sumOfEntries = entries.foldLeft(0.0d)((s: Double, e: Entry) => s + e.amount.total)
    if (sumOfEntries == 0.0d) Pass(entries)
    else Fail("Sum of entries should equal 0",sumOfEntries)
  }

}

final class TransactionUnitValidator extends Validator {
  def validate(what: List[Any]): TestResult =
    atLeastOneCategoryWasGiven(what.asInstanceOf[List[TransactionUnit]])

  private def atLeastOneCategoryWasGiven(transactionUnits: List[TransactionUnit]): TestResult = {
    if (transactionUnits.nonEmpty) Pass(transactionUnits)
    else Fail("At least one category is required", transactionUnits)
  }
}
