package businesslogic
package transaction

import validation._

/**
  * This is the control class which restricts access to the creation of entries
  * on categories.
  * Here the dependencies to the validators with default parameters were passed
  * to the constructor in case the validation and transaction aspect of the
  * code can be executed in multiple threads
  */
class Transaction(validators: List[Validator] = List(
  new TransactionEntriesValidator, new TransactionUnitValidator
)) {

  /**
    * This method validates and executes a transaction executed by a user
    * Decided to use dependency injection on method here, so that one Transaction
    * object can still be reused with multiple Category/Entries pairs
    */
  def execute(transactionUnits: List[TransactionUnit]): List[Category] = {
    validators.foreach({vdr: Validator =>
      // TODO: make this more meaningful -- not using the TestResults at all
      vdr.validate(transactionUnits) match {
        case Fail(m: String, _) => throw new IllegalArgumentException(m)
        case _ => Unit
      }
    })
    transactionUnits.map(addEntries)
  }

  private def addEntries(tu: TransactionUnit): Category = {
    val cat: Category = tu.category
    val ents: List[Entry] = tu.entries
    if (cat.entries.isEmpty) cat.copy(entries = ents)
    else cat.copy(entries = cat.entries ++ ents)
  }
}
