package personalfinance
package businesslogic
package transaction
package dates

import org.joda.time.DateTime

trait DateStringParser {
  def dateFromString(date: String): DateTime
}
