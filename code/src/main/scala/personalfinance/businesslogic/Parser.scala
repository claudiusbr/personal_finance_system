package personalfinance
package businesslogic

trait Parser[T] {
  def parseCSVLines(lines: Iterable[String], props: PropertiesLoader): Iterable[T]
}
