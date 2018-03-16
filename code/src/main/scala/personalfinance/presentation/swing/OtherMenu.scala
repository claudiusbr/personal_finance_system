package personalfinance
package presentation
package swing

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

  /**
    * returns a vertical box with a date field
    * @param title the title of the box. "Date" by default
    */
  protected def getDateBox(title: String = "Date"): BoxPanel =
    new BoxPanel(Orientation.Vertical) {
      contents ++= Array(
        new Label(title),
        Swing.VGlue,
        new TextField {columns = 15}
      )
    }

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

