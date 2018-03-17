package personalfinance
package businesslogic

import personalfinance.input.PropertiesLoader

trait Parser[T] {
  def parseCSVLines(lines: Iterable[String], props: PropertiesLoader): Iterable[T]
}
