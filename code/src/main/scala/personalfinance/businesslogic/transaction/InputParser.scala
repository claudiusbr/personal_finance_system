package personalfinance.businesslogic.transaction

trait InputParser[T] {
  def parseLines(lines: Iterable[String], props: PropertiesLoader): Iterable[T]
}
