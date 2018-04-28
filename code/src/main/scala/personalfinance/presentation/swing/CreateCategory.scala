package personalfinance.presentation.swing


import scala.swing._
import scala.swing.event._

import java.awt.Font

private[swing] class CreateCategory(entryType: String, date: String, description: String,
                                    amount: String, fontSpecs: Font, main: MainMenu,
                                    mediator: SwingMediator) extends MainFrame {

  protected val cancelBtn = new Button("Back")
  protected val okBtn = new Button("OK")

  protected val me: CreateCategory = this

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
      main.location = this.location
      main.visible = true
      this.visible = false

    case WindowActivated(`me`) => this.size = main.size
  }

}
