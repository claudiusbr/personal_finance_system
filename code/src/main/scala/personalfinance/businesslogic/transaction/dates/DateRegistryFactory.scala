package personalfinance
package businesslogic
package transaction
package dates

class DateRegistryFactory {
  def getDateRegistry(date: String, df: DateFormatter =
    new DateFormatter): DateRegistry = new DateRegistry(df.dateFromString(date))
}