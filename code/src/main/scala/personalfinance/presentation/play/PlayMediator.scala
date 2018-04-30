package personalfinance
package presentation
package play

trait PlayMediator {

  /**
    * The types of manual entries which can be created.
    * @return a sequence of flag values understood by the business logic
    */

  def entryTypes: Seq[String]

  def requestMonthlyBudget(): Unit

  def requestYearlyBudget(): Unit

  /**
    * displays the budget, which is given as a list of category
    * names plus the total
    * @param budget a Seq of Tuple3, of which the head is the
    *               budgeted income, and where it and all
    *               other lines consist of:
    *               budget._1 the category name
    *               budget._2 the budgeted amount
    *               budget._3 the percentage of income
    */
  def displayBudget(budget: Seq[(String,Double,Double)]): Unit

  def requestSummary(from: String, to: String): Unit

  def displaySummary(from: String, to: String, summary: Seq[(String,Double)]): Unit

  def warnUser(message: String): Unit

  def informUser(message: String): Unit

  def sendConfirmationMessage(message: String): Unit

  def createManualEntry(entryType: String, date: String, description: String, total: String,
                        breakdown: Seq[Map[String,String]]): Unit

  def classifyWithNewCategory(catName: String, catPattern: String,
                              entries: Seq[(String,String,String,String)]): Unit

  /**
    * this method takes a list of entries as a Tuple4[String] and
    * asks the user for a category for the first entry
    * @param entries a Tuple4[String] where:
    *                entries._1 is the entry type
    *                entries._2 is the date
    *                entries._3 is the description
    *                entries._4 is the amount
    */
  def requestCategoryFromUser(entries: Seq[(String,String,String,String)]): Unit

  def getAllCategoryNames: Seq[String]

  def uploadStatement(filePath: String): Unit
}
