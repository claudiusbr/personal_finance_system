package personalfinance.presentation.swing

import java.awt.{Dimension, Font}

import scala.swing.{Dialog, Reactor}

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

  def displaySummary(from: String,to: String, summary: Seq[(String,Double)]): Unit = {
    val head =
      "<html><table style='font-family:monospace; width:400px;'><tr><th>Category</th><th>Amount</th></tr>"

    val (headAndbody: String, sum: Double) = summary.reverse.foldLeft((head,0.0))((strSum,catAndAmt) => {
      (strSum._1 + s"<tr><td>${catAndAmt._1}</td><td>${catAndAmt._2.toString}</td></tr>",
        strSum._2 + catAndAmt._2)
    })

    val foot = s"<tr><th>Total</th><th>$sum</th></tr></table></html>"

    Dialog.showMessage(message=headAndbody+foot)
  }

  /** Finalizes the application by calling `shutdown` and exits.*/
  def quit(): Unit = { sys.exit(0) }
}
