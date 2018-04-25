package personalfinance

import presentation._
import businesslogic._
import transaction._
import dates._
import input._
import personalfinance.validation._

/**
  * this object handles the interactions between the application logic and
  * the presentation layer. It hands over all interactions with the persistence
  * layer to the PersistenceMediator
  */
object InteractionMediator extends PresentationMediator with Mediator {
  private val frontEndChoice: String = propertiesLoader
    .getProperty("currentfrontend")
  private val presentationAmbassador =
    PresentationFactory.getPresentationAmbassador(frontEndChoice, this)

  private val dateRegistryFactory = new DateRegistryFactory

  override def startup(): Unit = {
    PersistenceMediator.startup()
    presentationAmbassador.startup()
  }

  override def entryTypes: Seq[String] = EntryType.values.map(_.toString).toSeq

  override def calculateBudget(): Unit = ???

  override def viewSummary(from: String, to: String): Unit = ???

  override def uploadStatement(filePath: String): Unit = {
    val input = new Input
    val validator = new InputValidator
    val linesFromFile: Seq[String] = validator.validate(input.lines(filePath)) match {
      case Pass(lines) => lines.asInstanceOf[Seq[String]]
      case Fail(message, _) => throw new RuntimeException(message)
    }

    val entries: Seq[Entry] = {
      val headers: Array[String] = linesFromFile.head.split(",")
      val date: Int = headers.indexOf("date")
      val description: Int = headers.indexOf("description")
      val amount: Int = headers.indexOf("amount")

      linesFromFile.tail.map({ tuple => {
          val tmp: Array[String] = tuple.split(",")
          Entry(
            Amount(tmp(amount).toDouble),
            dateRegistryFactory.getDateRegistry(tmp(date)),
            tmp(description))
        }
      })
    }

    val categories: Seq[Category] = PersistenceMediator.getAllCategoriesAndPatterns()
    val classifier = new Classifier
    val (readyToCommitTUs,uncategorised): (Seq[TransactionUnit], Seq[Entry]) =
      classifier.classify(categories,entries)

    /**
      * Think of categorymaker
      * for each uncategorised: return a list of categorised
      *   - ask user for category and pattern
      *   - existing category?
      *     - add pattern to category
      *   - new category?
        *   - create category with pattern
      *   - get categoryId
      *   - run every other uncategorised through new pattern
      *     - if any matches, remove it from list of uncategorised
      */
  }

  override def createManualEntry(entryType: String, date: String,
                                 description: String, total: String,
                                 breakdown: Seq[Map[String,String]]): Unit = {

    // TODO: improve this: it would be inefficient with a non-local database
    // Since the bank category is static, it could be optimised
    val bankCategory = PersistenceMediator.getOrMakeCategory("Bank")

    val bankTotal: Double = convertAmountForBank(total, entryType)

    val transactionDate = dateRegistryFactory.getDateRegistry(date)

    val bankEntry = Entry(Amount(bankTotal),transactionDate,description)

    val bankTU: TransactionUnit = TransactionUnit(bankCategory,List(bankEntry))
    val breakDownTUs: Seq[TransactionUnit] = breakdown.map(
      mapBkdn => {
        val entry = Entry(
          Amount(convertAmountForBreakdown(mapBkdn("amount"),entryType)),
          transactionDate,
          description
        )
        // TODO: add a different implementation for this: as it stands, it could not
        // be re-used, since the instances of Category would always be recreated here
        // perhaps make this variable be populated from a function argument which
        // gets passed by the caller -- this way it would be more dynamic
        val bkdnCategory: Category = PersistenceMediator.getOrMakeCategory(mapBkdn("category"))
        TransactionUnit(bkdnCategory,List(entry))
      }
    )

    val transaction = new Transaction
    val transactionResult: Seq[Category] = transaction.execute(bankTU +: breakDownTUs)
    saveTransaction(transactionResult)
  }


  override def createCategoryUI(entryType: String, date: String, description: String, amount: String): Unit = ???

  private def saveTransaction(cats: Seq[Category]): Unit = {
    PersistenceMediator.commitTransactionToDB(cats)
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
