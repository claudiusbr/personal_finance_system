package personalfinance
package businesslogic
package transaction
import personalfinance.input.PropertiesLoader

class EntryParser(drf: DateRegistryFactory = new DateRegistryFactory)
  extends Parser[Entry] {
  def parseCSVLines(lines: Iterable[String], props: PropertiesLoader): Iterable[Entry] = {
    val headers = lines.head.split(",")

    val dateHeader: String = props.getProperty("date")
    val descriptionHeader: String = props.getProperty("description")
    val amountHeader: String = props.getProperty("amount")

    class ColumnName(val header: String, val index: Int)

    val date = new ColumnName(dateHeader, headers.indexOf(dateHeader))
    val description = new ColumnName(descriptionHeader, headers.indexOf(descriptionHeader))
    val amount = new ColumnName(amountHeader, headers.indexOf(amountHeader))

    lines.tail.map(
      (line: String) => {
        val contents = line.split(",")
        Entry(
          amount = contents(amount.index).toDouble,
          dates = drf.getDateRegistry(contents(date.index)),
          description = contents(description.index)
        )
      }
    )
  }

}
