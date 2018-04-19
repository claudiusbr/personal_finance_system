package personalfinance

import personalfinance.persistence.PersistenceBridge
import presentation.Presentation

object App {
  private val propertiesLoader = new PropertiesLoader("config.properties")

  def main(args: Array[String]): Unit = {
    val presentation = new Presentation(propertiesLoader)
    presentation.start()
  }
}
