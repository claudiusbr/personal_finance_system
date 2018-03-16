package personalfinance
package businesslogic
package transaction

import org.joda.time.DateTime

object Entry {
  def apply(amount: Double,dates: DateRegistry, description: String): Entry =
    apply(Amount(amount), dates, description)

  def apply(amount: Double, currency: Currency, dates: DateRegistry, description: String): Entry =
    apply(Amount(amount,currency),dates, description)
}

case class Entry(amount: Amount, private val dates: DateRegistry, description: String) {

  def this(amount: Double, dates: DateRegistry, description: String) =
    this(Amount(amount),dates, description)

  def this(amount: Double, currency: Currency, dates: DateRegistry, description: String) =
    this(Amount(amount,currency),dates, description)

  val dateCreated: DateTime = dates.dateCreated
  val dateRecorded: DateTime = dates.dateRecorded
}
