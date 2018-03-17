package personalfinance
package input

class InputTester extends BehaviourTester {
  val in = new Input
  "an Input object" should "try to load a file from the filesystem and return a Some if it finds it" in {
    in.lines("./src/test/testtextfile") should be (Some(Iterable(
          "date,description,amount",
          "2018-01-03,blabla,200.00",
          "2018-01-04,bleble,-10.00")))
  }

  it should "return a None if it does not find it" in {
    in.lines("./I-don't-exist") should be (None)
  }
}
