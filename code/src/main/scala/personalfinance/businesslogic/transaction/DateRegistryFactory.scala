package personalfinance.businesslogic.transaction

class DateRegistryFactory {
  def getDateRegistry(date: String, df: DateFormatter =
    new DateFormatter): DateRegistry = new DateRegistry(df.dateFromString(date))
}
