package personalfinance
package businesslogic
package transaction
package dates

import org.joda.time.DateTime

/**
  * This class records date created and date recorded
  */
class DateRegistry private[dates](val dateCreated: DateTime,
                                  val dateRecorded: DateTime = DateTime.now())
