package personalfinance

import java.io.FileNotFoundException

import presentation._
import businesslogic._
import transaction._
import dates._
import input._
import org.joda.time.DateTime
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
    PresentationBuilder.getPresentationAmbassador(frontEndChoice, this)

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

  override def requestMonthlyBudget(): Unit = {
    val to: DateTime = dateRegistryFactory.getDateRegistry("now").dateCreated
    val yearPrior: DateTime = to.minusYears(1)
    val balance: Seq[(String,Double)] = PersistenceMediator.getSummary(yearPrior,to)
    val budget: Seq[(String,Double,Double)] = getBudget(balance)
    val monthlyBudget: Seq[(String,Double,Double)] =
      budget.map(tuple => {tuple.copy(_2 = tuple._2/12.0)})
    displayBudget(monthlyBudget)
  }

  override def requestYearlyBudget(): Unit = {
    val to: DateTime = dateRegistryFactory.getDateRegistry("now").dateCreated
    val from: DateTime = to.minusYears(5)
    val balance: Seq[(String,Double)] = PersistenceMediator.getSummary(from,to)
    val budget: Seq[(String,Double,Double)] = getBudget(balance)
    val yearlyBudget: Seq[(String,Double,Double)] =
      budget.map(tuple => {tuple.copy(_2 = tuple._2/5.0)})
    displayBudget(yearlyBudget)
  }

  override def displayBudget(budget: Seq[(String, Double, Double)]): Unit =
    presentationAmbassador.displayBudget(budget)

  override def requestSummary(from: String, to: String): Unit = {
    val f: DateTime = dateRegistryFactory.getDateRegistry(from).dateCreated
    val t: DateTime =
      if (to == "") dateRegistryFactory.getDateRegistry("now").dateCreated
      else dateRegistryFactory.getDateRegistry(to).dateCreated
    val summary: Seq[(String,Double)] = PersistenceMediator.getSummary(f,t)
    displaySummary(from,to, summary)
  }

  override def displaySummary(from: String, to: String, summary: Seq[(String, Double)]): Unit = {
    presentationAmbassador.displaySummary(from,to,summary)
  }


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


  override def requestCategoryFromUser(entries: Seq[(String,String,String,String)]): Unit = {
    presentationAmbassador.requestCategoryFromUser(entries)
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
    val classifier: Classifier = new StringClassifier
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


  override def getAllCategoryNames: Seq[String] = PersistenceMediator
    .getAllCategoriesAndPatterns().map { _.name }.sorted


  override def warnUser(message: String): Unit = presentationAmbassador.warnUser(message)

  override def informUser(message: String): Unit = presentationAmbassador.informUser(message)

  override def sendConfirmationMessage(message: String): Unit = presentationAmbassador.informUser(message)

  private def saveTransaction(cats: Seq[Category]): Unit = {
    PersistenceMediator.commitTransactionToDB(cats)
  }

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
      requestCategoryFromUser(toSendToUser)
    }
  }

  private def getBudget(balance: Seq[(String,Double)]): Seq[(String,Double,Double)] = {
    val (income,expenditure): (Seq[(String,Double)],Seq[(String,Double)]) =
      balance.partition(_._2 > 0)

    case class BudgetTuple(name: String, amount: Double, percentageOfIncome: Double) {
      def toTuple: (String,Double,Double) = (name,amount,percentageOfIncome)
    }

    val incomeForPeriod = BudgetTuple(
      "Income", income.foldLeft(0.0)((sum,catAndAmt) => { sum + catAndAmt._2 }), 100.0)

    val expenditureForPeriod: Seq[BudgetTuple] = expenditure.map { catAndAmt => {
      BudgetTuple(catAndAmt._1, catAndAmt._2, Math.abs(
        100.0 * catAndAmt._2 / incomeForPeriod.amount))
    }
    }

    /* the prepend method (+:) was chosen due to being O(1) efficient
       when dealing with linear Sequences in Scala
       (Odersky et al, 2016, Location 14469)
     */
    val budgetForPeriod = expenditureForPeriod.sortBy(-1 * _.amount)
      .foldLeft(Seq(incomeForPeriod))((budg,exp) => {
        budg match {
          case _ :: Nil => exp +: budg

          case x :: accBudg if x.percentageOfIncome < 20.0
            && x.percentageOfIncome + exp.percentageOfIncome <= 20.0 =>
            BudgetTuple("Other", x.amount + exp.amount,
              x.percentageOfIncome + exp.percentageOfIncome) +: accBudg

          case _ => exp +: budg
        }
      }).reverse

    val budgetExpenditure: Double = budgetForPeriod.tail.foldLeft(0.0)(_ + _.amount)
    budgetForPeriod match {
      case budgetIncome :: exp if budgetIncome.amount < budgetExpenditure =>
        (budgetIncome.copy(amount=budgetExpenditure) +: exp).map(_.toTuple)

      case _ => budgetForPeriod.map(_.toTuple)
    }
  }
}
