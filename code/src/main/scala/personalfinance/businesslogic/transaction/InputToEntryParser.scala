package personalfinance
package businesslogic
package transaction

import personalfinance.businesslogic.transaction.dates.DateRegistryFactory

import scala.util.matching.Regex

class InputToEntryParser(drf: DateRegistryFactory = new DateRegistryFactory)
  extends InputParser[Entry] {

  def parseLines(lines: Iterable[String], props: PropertiesLoader): Iterable[Entry] = {
    val headers = lines.head.split(",")

    val dateHeader: String = props.getProperty("date")
    val descriptionHeader: String = props.getProperty("description")
    val amountHeader: String = props.getProperty("amount")

    class ColumnName(val header: String, val index: Int)

    val date = new ColumnName(dateHeader, headers.indexOf(dateHeader))
    val description = new ColumnName(descriptionHeader, headers.indexOf(descriptionHeader))
    val amount = new ColumnName(amountHeader, headers.indexOf(amountHeader))

    // TODO: validate input
    lines.tail.map(
      (line: String) => {
        val contents = replCommas(line).split(",")
        Entry(
          amount = Amount(contents(amount.index).toDouble),
          dates = drf.getDateRegistry(contents(date.index)),
          description = contents(description.index)
        )
      }
    )
  }

  /**
    * replace interpolated commas with spaces
    * @param text the text to replace the commas
    * @return the same text with commas replaced
    */
  private def replCommas(text: String): String = {
    // this is needed to avoid wrong splitting of any line
    val commaInText: Regex = "(.*\".*),(.*\".*)".r

    // this is where the regular expression comes in
    val formatted = commaInText.replaceAllIn(text, _ => s"$$1 $$2")

    commaInText.findFirstIn(formatted) match {
      case Some(e) => replCommas(e)
      case None => formatted
    }
  }
}
