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

  def sumEqualsZero(entries: List[Entry]): Boolean =
    entries.foldLeft(0.0d)((s: Double, e: Entry) => s + e.amount.total) == 0.0d
}
