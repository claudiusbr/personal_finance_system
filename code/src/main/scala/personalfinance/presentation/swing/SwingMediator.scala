package personalfinance
package presentation
package swing

trait SwingMediator {
  def calculateBudget(): Unit

  def viewSummary(from: String, to: String): Unit

  def createManualEntry(entryType: String, date: String, total: String,
                        breakdown: Seq[Map[String,String]]): Unit

  def uploadStatement(filePath: String): Unit
}
