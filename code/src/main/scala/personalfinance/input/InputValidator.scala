package personalfinance
package input

import businesslogic.transaction.validation._

class InputValidator extends Validator {
  override def validate(what: Seq[Any]): TestResult =
    validateType( "An Iterable[String] argument is required",
      what,(p: Any) => validateInput(p.asInstanceOf[Seq[String]]))

  private def validateInput(input: Seq[String]): TestResult = {
    val firstLine = input.head.toLowerCase
    firstLine match {
      case "date,description,amount" => Pass(input)
      case _ =>
        Fail("First line of CSV should be in the format:"+
          " date,description,amount",input)
    }
  }
}
