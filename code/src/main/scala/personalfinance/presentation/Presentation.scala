package personalfinance
package presentation

class Presentation(propertiesLoader: PropertiesLoader) {
  private val frontEndChoice: String = propertiesLoader.getProperty("currentfrontend")
  private val frontEnd: PresentationBridge =
    Class.forName(s"personalfinance.presentation.$frontEndChoice")
      .getConstructor().newInstance().asInstanceOf[PresentationBridge]

  def start(): Unit = frontEnd.startup()
}