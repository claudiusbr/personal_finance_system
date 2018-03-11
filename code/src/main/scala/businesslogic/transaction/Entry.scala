package businesslogic.transaction

import org.joda.time.DateTime

object Entry {
  def apply(amount: Double,dateCreated: DateTime, dateRecorded: DateTime,
            description: String): Entry =
    apply(Amount(amount), dateCreated, dateRecorded, description)

  def apply(amount: Double, currency: Currency, dateCreated: DateTime,
            dateRecorded: DateTime, description: String): Entry =
    apply(Amount(amount,currency),dateCreated,dateRecorded, description)
}

case class Entry(amount: Amount, dateCreated: DateTime,
                 dateRecorded: DateTime, description: String) {

  def this(amount: Double,
           dateCreated: DateTime,
           dateRecorded: DateTime,
           description: String) =
    this(Amount(amount),dateCreated,dateRecorded, description)

  def this(amount: Double,
           currency: Currency,
           dateCreated: DateTime,
           dateRecorded: DateTime,
           description: String) =
    this(Amount(amount,currency),dateCreated,dateRecorded, description)
}
