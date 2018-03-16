package personalfinance
package businesslogic
package transaction

sealed trait Currency
case object GBP extends Currency
case object EUR extends Currency
case object USD extends Currency