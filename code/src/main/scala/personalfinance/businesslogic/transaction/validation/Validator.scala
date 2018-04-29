package personalfinance
package businesslogic
package transaction
package validation

trait Validator {
  def validate(what: Seq[Any]): TestResult

  /**
    * this is a method mainly to aid the implementation of the `validate` method.
    * Since that method uses a generic type, it was felt that it would also be useful
    * to have functionality which would check if the type which would be passed to it
    * would be correct, and return the right TestResult if not, instead of throwing
    * a ClassCast exception
    * @param messageIfItFails the message to be returned if the test fails
    * @param arg the argument to be tested by the function argument
    * @param p the function to test `arg` which also tries to type cast
    * @return a test result from p, or Fail with the message if it fails type check
    */
  protected def validateType(messageIfItFails: String, arg: Any, p: Any => TestResult): TestResult =
    try { p(arg) }
    catch {
      case _: ClassCastException => Fail(messageIfItFails,arg)
      case e: Throwable => throw e
    }
}


