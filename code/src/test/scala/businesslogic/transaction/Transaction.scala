package businesslogic.transaction

/**
  * This is the control class which restricts access to the creation of entries
  * on categories.
  */
class Transaction {

  /**
    * This method validates and executes a transaction executed by a user
    */
  //def execute(operands: List[Tuple2[Category,List[Entry]]]): List[Category] = { }

  /**
    * this method takes a list of entries and adds them up to make sure they
    * equal zero, thus preserving double entry
    * @param entries a list of entries
    * @return true if the sum of the amounts of all entries equals zero
    */
  def sumEqualsZero(entries: List[Entry]): Boolean =
    entries.foldLeft(0.0d)((s: Double, e: Entry) => s + e.amount.total) == 0.0d
}
