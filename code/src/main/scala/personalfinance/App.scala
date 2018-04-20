package personalfinance

import personalfinance.persistence.PersistenceBridge
import presentation.Presentation

object App {

  def main(args: Array[String]): Unit = {
    val propertiesLoader = new PropertiesLoader("config.properties")
    val privateLoader = new PropertiesLoader("private.properties")

    val frontEndChoice: String = propertiesLoader.getProperty("currentfrontend")
    val presentationBridge = Presentation.getBridge(frontEndChoice)

    val persistenceBridge = new PersistenceBridge(propertiesLoader,privateLoader)

    presentationBridge.startup()
  }
}
