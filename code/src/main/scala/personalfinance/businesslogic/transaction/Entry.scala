package personalfinance
package businesslogic
package transaction

import org.joda.time.DateTime
import personalfinance.businesslogic.transaction.dates.DateRegistry

/**
  * This class has very busy constructors and companion objects, to ensure flexibility.
  * The amount parameter can be given with just a double value, or as Double and
  * Currency, and they will all compile to an Amount entity.
  * @param amount an instance of the Amount class
  * @param dates an instance of the DateRegistry class
  * @param description a description of the entry in question
  */
case class Entry(amount: Amount, private val dates: DateRegistry,
                 description: String, id: Option[Int] = None) {

  val dateCreated: DateTime = dates.dateCreated
  val dateRecorded: DateTime = dates.dateRecorded
}
