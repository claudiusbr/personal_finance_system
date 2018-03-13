package businesslogic
package transaction

import org.joda.time.DateTime

/**
  * This class records date created and date recorded
  */
class DateRegistry(val dateCreated: DateTime, val dateRecorded: DateTime)
