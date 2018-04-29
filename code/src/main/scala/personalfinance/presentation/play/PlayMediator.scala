package personalfinance
package presentation
package play

trait PlayMediator {

  /**
    * The types of manual entries which can be created.
    * @return a sequence of flag values understood by the business logic
    */

  def entryTypes: Seq[String]

  def calculateBudget(): Unit

  def getSummary(from: String, to: String): Unit

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
  def getCategoryFromUser(entries: Seq[(String,String,String,String)])

  def getAllCategoryNames(): Seq[String]

  def uploadStatement(filePath: String): Unit
}
