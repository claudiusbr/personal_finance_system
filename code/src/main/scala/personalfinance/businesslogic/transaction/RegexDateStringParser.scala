package personalfinance
package businesslogic
package transaction

import scala.util.matching.Regex
import org.joda.time.DateTime

import scala.language.implicitConversions

class RegexDateStringParser extends dates.DateStringParser {

  private val slashDotDashYYYYMMDD: Regex = """(\d{4})(/|\.|-)(\d{2})(/|\.|-)(\d{2})""".r
  private val slashDotDashDDMMYYYY: Regex = """(\d{2})(/|\.|-)(\d{2})(/|\.|-)(\d{4})""".r

  def dateFromString(date: String): DateTime = {
    implicit def stringToInt(s: String): Int = new StringBuilder(s).toInt

    date match {
      case slashDotDashYYYYMMDD(year, _, month, _, day) => new DateTime(year, month, day,0,0)
      case slashDotDashDDMMYYYY(day, _, month, _, year) => new DateTime(year,month,day,0,0)
      case _ => throw new IllegalArgumentException("Unsupported date format")
    }
  }

}
