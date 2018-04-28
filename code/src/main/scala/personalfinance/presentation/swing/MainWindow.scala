package personalfinance.presentation.swing

import java.awt.{Dimension, Font}

import scala.swing.Reactor

class MainWindow(mediator: SwingMediator) extends Reactor {
  private val verdana: Font = new Font("Verdana", Font.BOLD, 20)

  private val mainMenu = new MainMenu(verdana, mediator)

  def createCategory(entries: Seq[(String,String,String,String)]): Unit =
    mainMenu.createCategory(entries)

  def startup(): Unit = {
    if (mainMenu.size == new Dimension(0,0)) mainMenu.pack()
    mainMenu.visible = true
  }

  /** Finalizes the application by calling `shutdown` and exits.*/
  def quit(): Unit = { sys.exit(0) }
}
