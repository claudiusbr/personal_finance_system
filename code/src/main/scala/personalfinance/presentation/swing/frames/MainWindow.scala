package personalfinance.presentation.swing.frames

import java.awt.{Dimension, Font}

import personalfinance.presentation.swing.SwingMediator

import scala.swing.Reactor

class MainWindow(mediator: SwingMediator) extends Reactor {
  private val verdana: Font = new Font("Verdana", Font.BOLD, 20)

  private val mainMenu = new MainMenu(verdana, mediator)

  def startup(): Unit = {
    if (mainMenu.size == new Dimension(0,0)) mainMenu.pack()
    mainMenu.visible = true
  }

  /** Finalizes the application by calling `shutdown` and exits.*/
  def quit(): Unit = { sys.exit(0) }
}
