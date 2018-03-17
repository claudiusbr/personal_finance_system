package personalfinance
package input

class PropertiesLoaderTester extends BehaviourTester {
  val pl = new PropertiesLoader("./src/test/testprops")
  "a PropertiesLoader" should "load properties from a file when requested" in {
    pl getProperty "test"  should be ("result")
  }
}
