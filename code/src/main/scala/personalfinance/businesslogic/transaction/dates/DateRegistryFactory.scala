package personalfinance
package businesslogic
package transaction
package dates

class DateRegistryFactory {
  def getDateRegistry(date: String, df: DateStringParser =
    new RegexDateStringParser): DateRegistry = new DateRegistry(df.dateFromString(date))
}