package personalfinance

import presentation._
import businesslogic._
import transaction._
import dates._

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

    val bankTotal: Double = convertAmountForBank(total, entryType)

    val transactionDate = dateRegistryFactory.getDateRegistry(date)

    val bankEntry = new Entry(bankTotal,transactionDate,description)
    val bankCategory = PersistenceMediator.getOrMakeCategory("Bank")

    val bankTU: TransactionUnit = TransactionUnit(bankCategory,List(bankEntry))
    val breakDownTUs: Seq[TransactionUnit] = breakdown.map(
      mapBkdn => {
        val entry = new Entry(
          convertAmountForBreakdown(mapBkdn("amount"),entryType),
          transactionDate,
          description
        )
        val bkdnCategory: Category = PersistenceMediator.getOrMakeCategory(mapBkdn("category"))
        TransactionUnit(bkdnCategory,List(entry))
      }
    )

    val transaction = new Transaction
    val transactionResult: Seq[Category] = transaction.execute(bankTU +: breakDownTUs)
    saveTransaction(transactionResult)
  }

  private def saveTransaction(cats: Seq[Category]): Unit = {
    cats.foreach {
      cat => {
        // TODO
        println(cat)
      }
    }
  }

  /**
    * on the Swing implementation, this is being called by MainWindow,
    * which will then stop the system. This is because MainWindow is the
    * reactor, which detects when the user closes the window.
    */
  override def quit(): Unit = PersistenceMediator.quit()


  /**
    * this method converts the amount entered by the user
    * as String to a positive double, if Income, or negative if expenditure.
    * @param amt the amount entered by the user
    * @return the amount converted to a signed double
    */
  private def convertAmountForBank(amt: String, entryType: String): Double =
    EntryType.values.find(_.toString == entryType) match {
      case Some(t) => t match {
        case EntryType.expenditure => -1.0 * amt.toDouble
        case EntryType.income => amt.toDouble
      }
      case None =>
        throw new IllegalArgumentException(
          "invalid entry type passed by ManualEntry frontend")
    }

  /**
    * this is the counterpart of the above, except that it converts the amount
    * to positive if Expenditure, and negative if Income -- this is the `other side`
    * of the double entry with the bank
    * @param amt the amount entered by the user
    * @return the amount converted to a signed double
    */
  private def convertAmountForBreakdown(amt: String, entryType: String): Double =
    -1 * convertAmountForBank(amt, entryType)

}
