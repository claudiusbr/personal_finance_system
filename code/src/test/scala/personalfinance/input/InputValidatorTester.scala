package personalfinance
package input

import validation.{Pass,Fail}

class InputValidatorTester extends BehaviourTester {
  val iv: InputValidator = new InputValidator
  val notValid: List[String] = List("one", "two", "three")
  val valid: List[String] = List("date,description,amount") ++ notValid
  val invalidType: List[Int] = List(1,2,3)

  "An InputValidator" should "ensure the csv columns are correct" in {
    iv.validate(valid) should be (Pass(valid.head))
    iv.validate(notValid) should be (Fail("First line of CSV should" +
      " be in the format: date,description,amount",notValid.head))
  }

  it should "ensure that the correct type argument has been passed" in {
    iv.validate(invalidType) should be (Fail("An Iterable[String]" +
      " argument is required",invalidType))
    iv.validate(invalidType.asInstanceOf[List[String]]) should be (Fail(
      "An Iterable[String] argument is required",invalidType))
  }
}
