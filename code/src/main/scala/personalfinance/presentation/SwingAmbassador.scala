package personalfinance
package presentation

import swing.frames.MainWindow

private[presentation] case class SwingAmbassador(mediator: PresentationMediator) extends PresentationMediator {
  private val mainWindow = new MainWindow(mediator)

  override def startup(): Unit = mainWindow.startup()

  override def calculateBudget(): Unit = ???

  override def viewSummary(from: String, to: String): Unit = ???

  override def uploadStatement(filePath: String): Unit = ???

  override def createManualEntry(entryType: String, date: String,
                                 total: String, breakdown: Seq[Map[String,String]]): Unit = ???

  override def quit(): Unit = mediator.quit()
}
