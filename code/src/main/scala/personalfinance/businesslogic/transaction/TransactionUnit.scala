package personalfinance
package businesslogic
package transaction

/**
  * This class is will contain the elements of one side of a transaction
  * Each transaction will need at least one transaction unit to proceed
  */
case class TransactionUnit(category: Category, entries: List[Entry])
