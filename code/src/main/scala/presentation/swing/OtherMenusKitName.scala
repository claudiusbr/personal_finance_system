package presentation
package swing

import java.awt.Font

import scala.swing._
import scala.swing.event._

abstract class OtherMenu(main: MainMenu) extends MainFrame {
  protected val cancelBtn = new Button("Back")
  protected val okBtn = new Button("OK")

  protected val me: OtherMenu = this

  protected val navigationBox: BoxPanel =
    new BoxPanel(Orientation.Horizontal) {
      contents += cancelBtn
      contents += Swing.HStrut(3)
      contents += okBtn
    }


  /**
    * an aid to set the maximum height of text fields and other components
    * @param comp a Component to set maximum height
    */
  protected def setMaxHeight(comp: Component): Unit =
    comp.maximumSize = new Dimension(Short.MaxValue, comp.preferredSize.height)

  listenTo(cancelBtn,okBtn, this)
  reactions += {
    case ButtonClicked(`cancelBtn`) =>
      val other = MainWindow.whatIsShowing
      main.location = other.location
      MainWindow.whatIsShowing = main
      MainWindow.whatIsShowing.visible = true
      other.visible = false

    case WindowActivated(`me`) => this.size = main.size
  }
}

private[swing] trait KitName

private[swing] case object ViewSummary extends KitName { val title = "View Summary" }
private[swing] class ViewSummary(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {
  title = ViewSummary.title
  contents = navigationBox
}

private[swing] case object CalculateBudget extends KitName { val title = "Calculate Budget" }
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {
  title = CalculateBudget.title
  contents = navigationBox
}

