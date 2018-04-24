package personalfinance
package businesslogic
package transaction

case class Amount(total: Double, currency: Currency = Currency("GBP", 1))