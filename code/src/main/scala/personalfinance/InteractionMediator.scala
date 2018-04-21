package personalfinance

import java.util.InputMismatchException

import presentation.{PresentationFactory, PresentationMediator}
import businesslogic.EntryType
import personalfinance.businesslogic.transaction.dates.DateRegistryFactory

/**
  * this object handles the interactions between the application logic and
  * the presentation layer. It hands over all interactions with the persistence
  * layer to the PersistenceMediator
  */
object InteractionMediator extends PresentationMediator with Mediator {
  private val frontEndChoice: String = propertiesLoader.getProperty("currentfrontend")
  private val presentationMediator =
    PresentationFactory.getPresentationAmbassador(frontEndChoice, this)

  private val dateRegistryFactory = new DateRegistryFactory

  override def startup(): Unit = {
    PersistenceMediator.startup()
    presentationMediator.startup()
  }

  override def entryTypes: Seq[String] = EntryType.values.map(_.toString).toSeq

  override def calculateBudget(): Unit = ???

  override def viewSummary(from: String, to: String): Unit = ???

  override def uploadStatement(filePath: String): Unit = ???

  override def createManualEntry(entryType: String, date: String, description: String,
                                 total: String, breakdown: Seq[Map[String,String]]): Unit = {
    /**
      * Create a transacitonUnit with the entry for the category chosen by the user,
      * then another with the opposite entry for bank, create a transaction,
      * and only if the transaction is valid then save it to the database
      *
      * this does not account for: a category that does not exist yet
      */

    val (bankTotal: Int, breakdownTotal: Int) =
      EntryType.values.find(_.toString == entryType) match {
        case Some(t) => t match {
          case EntryType.income => (-1 * total.toInt, total.toInt)
          case EntryType.expenditure => (total.toInt, -1 * total.toInt)
        }
        case None => throw new InputMismatchException("invalid entry type passed by ManualEntry frontend")
      }

    val transactionDate = dateRegistryFactory.getDateRegistry(date)

  }

  /**
    * on the Swing implementation, this is being called by MainWindow,
    * which will then stop the system. This is because MainWindow is the
    * reactor, which detects when the user closes the window.
    */
  override def quit(): Unit = PersistenceMediator.quit()
}
