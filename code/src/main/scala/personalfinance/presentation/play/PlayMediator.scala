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

  def viewSummary(from: String, to: String): Unit

  def createManualEntry(entryType: String, date: String, description: String, total: String,
                        breakdown: Seq[Map[String,String]]): Unit

  def uploadStatement(filePath: String): Unit
}
