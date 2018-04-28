package personalfinance
package businesslogic
package transaction
package validation

sealed trait TestResult
case class Pass(output: Any) extends TestResult
case class Fail(message:String, output: Any) extends TestResult
