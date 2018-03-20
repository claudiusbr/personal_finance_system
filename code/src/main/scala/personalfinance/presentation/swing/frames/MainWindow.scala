package personalfinance.presentation.swing.frames

import java.awt.{Dimension, Font}

import scala.swing.{Frame, SwingApplication}

object MainWindow extends SwingApplication {
  private val verdana: Font = new Font("Verdana", Font.BOLD, 20)

  private val mainMenu = new MainMenu(verdana)

  private[swing] var whatIsShowing: Frame = mainMenu

  override def startup(args: Array[String]): Unit = {
    if (whatIsShowing.size == new Dimension(0,0)) whatIsShowing.pack()
    whatIsShowing.visible = true
  }
}
