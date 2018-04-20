package personalfinance
package presentation

object Presentation {

  def getBridge(frontEndChoice: String): PresentationBridge = {
    Class.forName(s"personalfinance.presentation.$frontEndChoice")
      .getConstructor().newInstance().asInstanceOf[PresentationBridge]
  }

}