package personalfinance

import java.io.FileNotFoundException

import presentation._
import businesslogic._
import transaction._
import dates._
import input._
import validation._

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

  // TODO: improve this: it would be inefficient with a non-local database
  // Since the bank category is static, it could be optimised
  private lazy val bankCategory: Category =
    PersistenceMediator.getOrMakeCategory("Bank")

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
    val linesFromFile: Seq[String] = validator.validate(
      input.lines(filePath) match {
        case Some(lines) => lines
        case None => throw new FileNotFoundException("file not found")
      }
    ) match {
      case Pass(lines) => lines.asInstanceOf[Seq[String]]
      case Fail(message, _) => throw new RuntimeException(message)
    }
    val entries: Seq[Entry] = {
      val headers: Array[String] = linesFromFile.head.toLowerCase().split(",")
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

    categorise(entries)
  }


  override def createManualEntry(entryType: String, date: String,
                                 description: String, total: String,
                                 breakdown: Seq[Map[String,String]]): Unit = {

    val transactionDate: DateRegistry = dateRegistryFactory.getDateRegistry(date)

    val bankTransactionUnit: TransactionUnit =
      makeBankTransactionUnit(entryType,date,total,description)

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

        val _ = if (mapBkdn("pattern").nonEmpty) PersistenceMediator
          .createPattern(bkdnCategory.id.get,mapBkdn("pattern"))

        TransactionUnit(bkdnCategory,List(entry))
      }
    )

    val transaction = new Transaction
    val transactionResult: Seq[Category] = transaction.execute(bankTransactionUnit +: breakDownTUs)
    saveTransaction(transactionResult)
  }


  override def getCategoryFromUser(entries: Seq[(String,String,String,String)]): Unit = {
    presentationAmbassador.getCategoryFromUser(entries)
  }


  /** this method takes in a new category name, pattern and elements of possible
    * entries for that category, makes entries with them, and passes them on to
    * another method to be categorised
    * @param catName the name of the new category
    * @param catPattern the pattern that should match that category
    * @param entries a Tuple4[String] where:
    *                entries._1 is the entry type
    *                entries._2 is the date
    *                entries._3 is the description
    *                entries._4 is the amount
    */
  override def classifyWithNewCategory(catName: String, catPattern: String,
                                       entries: Seq[(String, String, String, String)]): Unit = {

    val c = PersistenceMediator.getOrMakeCategory(catName)
    val _ = PersistenceMediator.createPattern(c.id.get,catPattern)

    categorise(entries.map(e => {
      Entry(
        Amount(e._4.toDouble),
        dateRegistryFactory.getDateRegistry(e._2),
        e._3)
    }))
  }


  /**
    * this method takes uncategorised entries and matches their descriptions
    * against the patterns of existing categories in an enffort to categorise them.
    * for those where a match is found, it will commit transactions to the database against the bank,
    * and for those it cannot it will pass to the end user for new categories
    * @param entries a Seq[Entry] to be classified
    */
  private def categorise(entries: Seq[Entry]): Unit = {
    val categories: Seq[Category] = PersistenceMediator.getAllCategoriesAndPatterns()
    val classifier = new Classifier
    val (categorisedTransactionUnits,uncategorised): (Seq[TransactionUnit], Seq[Entry]) =
      classifier.classify(categories,entries)

    val readyToCommit = categorisedTransactionUnits.flatMap(tu => {
      val transaction = new Transaction()
      // because this transaction unit was generated by classifier
      // there is only one enetry in its 'enttries' member, but this
      // design could still be improved
      // TODO: improve classifier design to avoid uncertainty regarding entries member having more than one member
      val bankTransactionUnit: TransactionUnit =
        makeBankTransactionUnit(tu.entries.head)

      transaction.execute(Seq(bankTransactionUnit,tu))
    })

    if (readyToCommit.nonEmpty) PersistenceMediator.commitTransactionToDB(readyToCommit)

    if (uncategorised.nonEmpty) {
      classifyTheUnCategorised(uncategorised)
    } else {
      sendConfirmationMessage("Categorisation complete.")
    }
  }

  /**
    * on the Swing implementation, this is being called by MainWindow,
    * which will then stop the system. This is because MainWindow is the
    * reactor, which detects when the user closes the window.
    */
  override def quit(): Unit = PersistenceMediator.quit()

  private def makeBankTransactionUnit(entryType: String, date: String, total: String,
                                      description: String): TransactionUnit = {

    val bankTotal: Double = convertAmountForBank(total, entryType)
    val transactionDate = dateRegistryFactory.getDateRegistry(date)
    val entryForBank = Entry(Amount(bankTotal),transactionDate,description)

    TransactionUnit(bankCategory,Seq(entryForBank))
  }

  private def makeBankTransactionUnit(doubleEntryCouterpart: Entry): TransactionUnit = {
    val bankTotal: Double = -1 * doubleEntryCouterpart.amount.total
    val transactionDate: DateRegistry = doubleEntryCouterpart.dates
    val entryForBank = Entry(
      Amount(bankTotal),
      transactionDate,
      doubleEntryCouterpart
        .description)

    TransactionUnit(bankCategory,Seq(entryForBank))
  }


  private def saveTransaction(cats: Seq[Category]): Unit = {
    PersistenceMediator.commitTransactionToDB(cats)
  }

  override def getAllCategoryNames(): Seq[String] = PersistenceMediator
    .getAllCategoriesAndPatterns().map { _.name }.sorted

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


  private def classifyTheUnCategorised(uncategorised: Seq[Entry]): Unit = {
    if (uncategorised.nonEmpty) {
      val toSendToUser: Seq[(String, String, String, String)] = uncategorised.map(
        (e: Entry) => {
          val entryType: String = e.amount.total match {
            case a if a > 0.0 => EntryType.expenditure.toString
            case _ => EntryType.income.toString
          }

          val date: String = e.dateCreated.toString("dd/MM/YYYY")
          val description: String = e.description
          val amount: String = e.amount.total.toString

          (entryType, date, description, amount)
        })

      informUser("Please choose a category and pattern for this entry.")
      getCategoryFromUser(toSendToUser)
    }
  }

  override def warnUser(message: String): Unit = presentationAmbassador.warnUser(message)

  override def informUser(message: String): Unit = presentationAmbassador.informUser(message)

  override def sendConfirmationMessage(message: String): Unit = presentationAmbassador.informUser(message)
}
