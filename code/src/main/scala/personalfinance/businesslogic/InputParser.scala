package personalfinance
package businesslogic

trait InputParser[T] {
  def parseLines(lines: Iterable[String], props: PropertiesLoader): Iterable[T]
}
