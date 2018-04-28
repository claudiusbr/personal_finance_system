package personalfinance
package presentation

import personalfinance.presentation.swing.MainWindow

/**
  * This class exists in order to pass a direct reference to the swing package.
  * This is the only reason it exists -- it acts as a channel between the external
  * class that implements the PresentationMediator and the swing frontend.
  *
  * @param mediator an instance of the PresentationMediator trait
  */
private[presentation] case class SwingAmbassador(mediator: PresentationMediator) extends PresentationMediator {
  /**
    *
    */
  private val mainWindow = new MainWindow(mediator)


  override def startup(): Unit = mainWindow.startup()

  override def calculateBudget(): Unit = mediator.calculateBudget()

  override def viewSummary(from: String, to: String): Unit = mediator.viewSummary(from,to)

  override def uploadStatement(filePath: String): Unit = mediator.uploadStatement(filePath)

  override def createManualEntry(entryType: String, date: String, description: String,
                                 total: String, breakdown: Seq[Map[String,String]]): Unit =
    mediator.createManualEntry(entryType,date,description,total,breakdown)

  override def quit(): Unit = mediator.quit()

  override def entryTypes: Seq[String] = mediator.entryTypes

  override def getAllCategoryNames(): Seq[String] = mediator.getAllCategoryNames()

  /**
    * this method takes a list of entries as a Tuple4[String] and
    * asks the user for a category for the first entry
    *
    * @param entries a Tuple4[String] where:
    *                entries._1 is the entry type
    *                entries._2 is the date
    *                entries._3 is the description
    *                entries._4 is the amount
    */
  override def createCategoryUI(entries: Seq[(String, String, String, String)]): Unit = {
    mainWindow.createCategory(entries)
  }
}
