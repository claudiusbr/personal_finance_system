package personalfinance
package businesslogic
package transaction

import org.joda.time.DateTime
import personalfinance.businesslogic.transaction.dates.DateRegistry


object Entry {
  def apply(amount: Double,dates: DateRegistry, description: String): Entry =
    apply(Amount(amount), dates, description)

  def apply(amount: Double, currency: Currency, dates: DateRegistry, description: String): Entry =
    apply(Amount(amount,currency),dates, description)
}

/**
  * This class has very busy constructors and companion objects, to ensure flexibility.
  * The amount parameter can be given with just a double value, or as Double and
  * Currency, and they will all compile to an Amount entity.
  * @param amount an instance of the Amount class
  * @param dates an instance of the DateRegistry class
  * @param description a description of the entry in question
  */
case class Entry(amount: Amount, private val dates: DateRegistry, description: String) {
  def this(amount: Double, dates: DateRegistry, description: String) =
    this(Amount(amount),dates, description)

  def this(amount: Double, currency: Currency, dates: DateRegistry, description: String) =
    this(Amount(amount,currency),dates, description)

  val dateCreated: DateTime = dates.dateCreated
  val dateRecorded: DateTime = dates.dateRecorded
}
