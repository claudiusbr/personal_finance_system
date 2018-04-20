package personalfinance.presentation.swing.frames

import java.awt.{Dimension, Font}

import scala.swing.{Frame, Reactor, SwingApplication}

//object MainWindow extends SwingApplication {
object MainWindow extends Reactor {
  private val verdana: Font = new Font("Verdana", Font.BOLD, 20)

  private val mainMenu = new MainMenu(verdana)

  //private[swing] var whatIsShowing: Frame = mainMenu

  //override def startup(args: Array[String]): Unit = {
  def startup(): Unit = {
    if (mainMenu.size == new Dimension(0,0)) mainMenu.pack()
    mainMenu.visible = true
  }

  /** Finalizes the application by calling `shutdown` and exits.*/
  def quit(): Unit = { sys.exit(0) }
}
