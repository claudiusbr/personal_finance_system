package personalfinance.input

import personalfinance.validation._

class InputValidator extends Validator {
  override def validate(what: Iterable[Any]): TestResult =
    validateType( "An Iterable[String] argument is required",
      what,(p: Any) => validateInput(p.asInstanceOf[Iterable[String]]))

  private def validateInput(input: Iterable[String]): TestResult = {
    val firstLine = input.head.toLowerCase
    firstLine match {
      case "date,description,amount" => Pass(input)
      case _ =>
        Fail("First line of CSV should be in the format:"+
          " date,description,amount",firstLine)
    }
  }
}