package presentation
package swing
import java.awt.Font

import scala.swing._
import scala.swing.event.ButtonClicked

abstract class MenuShell(main: MainMenu) extends MainFrame {
  val cancelBtn = new Button("Back")
  val okBtn = new Button("OK")

  listenTo(cancelBtn,okBtn)
  reactions += {
    case ButtonClicked(`cancelBtn`) =>
      val other = MainWindow.whatIsShowing
      main.location = other.location
      MainWindow.whatIsShowing = main
      MainWindow.whatIsShowing.visible = true
      other.visible = false
  }
}

sealed trait KitName

private[swing] case object ManualEntry extends KitName
private[swing] class ManualEntry(fontSpecs: Font, main: MainMenu) extends MenuShell(main) {
  title = "Manual Entry"
  contents = new BoxPanel(Orientation.Horizontal) {
    contents += cancelBtn
    contents += Swing.HStrut(3)
    contents += okBtn
  }

}


private[swing] case object UploadStatement extends KitName
private[swing] class UploadStatement(fontSpecs: Font, main: MainMenu) extends MenuShell(main) {
  title = "Upload Statement"
  contents = new BoxPanel(Orientation.Horizontal) {
    contents += cancelBtn
    contents += Swing.HStrut(3)
    contents += okBtn
  }
}


private[swing] case object ViewSummary extends KitName
private[swing] class ViewSummary(fontSpecs: Font, main: MainMenu) extends MenuShell(main) {
  title = "View Summary"
  contents = new BoxPanel(Orientation.Horizontal) {
    contents += cancelBtn
    contents += Swing.HStrut(3)
    contents += okBtn
  }
}


private[swing] case object CalculateBudget extends KitName
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu) extends MenuShell(main) {
  title = "Calculate Budget"
  contents = new BoxPanel(Orientation.Horizontal) {
    contents += cancelBtn
    contents += Swing.HStrut(3)
    contents += okBtn
  }
}

