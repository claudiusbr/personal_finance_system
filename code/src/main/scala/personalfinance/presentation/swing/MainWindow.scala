package personalfinance.presentation.swing

import java.awt.{Dimension, Font}

import scala.swing.Reactor

class MainWindow(mediator: SwingMediator) extends Reactor {
  private val verdana: Font = new Font("Verdana", Font.BOLD, 20)

  private val mainMenu = new MainMenu(verdana, mediator)

  def createCategory(entries: Seq[(String,String,String,String)]): Unit =
    mainMenu.createCategory(entries)

  def warnUser(message: String): Unit = Messenger.warnUser(message)
  def informUser(message: String): Unit = Messenger.informUser(message)
  def sendConfirmationMessage(message: String): Unit = Messenger
    .sendConfirmationMessage(message)

  def startup(): Unit = {
    if (mainMenu.size == new Dimension(0,0)) mainMenu.pack()
    mainMenu.visible = true
  }

  def displayBudget(budget: Seq[(String, Double, Double)]): Unit =
    mainMenu.displayBudget(budget)

  def displaySummary(from: String,to: String, summary: Seq[(String,Double)]): Unit =
    mainMenu.displaySummary(from,to,summary)

  /** Finalizes the application by calling `shutdown` and exits.*/
  def quit(): Unit = { sys.exit(0) }
}
