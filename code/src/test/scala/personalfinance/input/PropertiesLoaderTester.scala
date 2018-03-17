package personalfinance
package input

/**
  * integration tester between PropertiesLoader and the local filesystem
  */
class PropertiesLoaderTester extends BehaviourTester {
  val pl = new PropertiesLoader("./src/test/testprops")
  "a PropertiesLoader" should "load properties from a file when requested" in {
    pl getProperty "test" should be ("result")
    pl getProperty "date" should be ("Date Created")
    pl getProperty "amount" should be ("20.00")
    pl getProperty "description" should be ("a bag of rice")
  }
}
