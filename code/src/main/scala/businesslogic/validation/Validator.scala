package businesslogic
package validation

trait Validator {
  def validate(what: List[Any]): TestResult
}


