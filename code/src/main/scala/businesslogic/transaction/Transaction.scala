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
class Transaction(entriesValidator: Validator = new TransactionEntriesValidator,
                  categoriesValidator: Validator = new TransactionUnitValidator) {

  /**
    * This method validates and executes a transaction executed by a user
    * Decided to use dependency injection on method here, so that one Transaction
    * object can still be reused with multiple Category/Entries pairs
    */
  /*
  def execute(transactionUnits: List[TransactionUnit]): List[Category] = {
    if (!(categoriesValidator validate transactionUnits))

    if (!(entriesValidator validate transactionUnits))
      throw new IllegalArgumentException(
        "\n"
      )
      */

}
