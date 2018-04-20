package personalfinance

import presentation.{PresentationFactory, PresentationMediator}
import persistence.PersistenceBridge
import personalfinance.input.Input

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
                                 total: String, breakdown: Seq[Map[String,String]]): Unit = {
    /**
      * Create a transacitonUnit with the entry for the category,
      * then another with the opposite entry for the bank, create a transaction,
      * and only if the transaction is valid then save it to the database
      *
      * this does not account for: a category that does not exist yet
      */
  }

  override def quit(): Unit = persistenceBridge.closeConnection()

}
