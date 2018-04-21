package personalfinance
package businesslogic

object EntryType extends Enumeration {
  type EntryType = Value
  val income: Value = Value("Income")
  val expenditure: Value = Value("Expenditure")
}
