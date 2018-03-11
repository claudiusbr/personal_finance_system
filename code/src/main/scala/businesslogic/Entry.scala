package businesslogic

object Entry {
  def apply(amount: Double): Entry = apply(Amount(amount))
  def apply(amount: Double,currency: Currency): Entry = apply(Amount(amount,currency))
}

case class Entry(amount: Amount) {
  def this(amount: Double) = this(Amount(amount))
  def this(amount: Double, currency: Currency) = this(Amount(amount,currency))
}
