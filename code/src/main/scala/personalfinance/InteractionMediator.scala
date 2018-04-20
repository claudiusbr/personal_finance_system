package personalfinance

import presentation.{PresentationFactory, PresentationMediator}
import persistence.PersistenceBridge

object InteractionMediator extends PresentationMediator {
  private val propertiesLoader = new PropertiesLoader("config.properties")
  private val privateLoader = new PropertiesLoader("private.properties")

  private val frontEndChoice: String = propertiesLoader.getProperty("currentfrontend")
  private val presentationMediator =
    PresentationFactory.getPresentationAmbassador(frontEndChoice, this)

  private val persistenceBridge = new PersistenceBridge(propertiesLoader,privateLoader)

  override def startup(): Unit = presentationMediator.startup()

  override def calculateBudget(): Unit = ???

  override def viewSummary(from: String, to: String): Unit = ???

  override def uploadStatement(filePath: String): Unit = ???

  override def createManualEntry(entryType: String, date: String,
                                 total: String, breakdown: Seq[Map[String,String]]): Unit = ???
}
