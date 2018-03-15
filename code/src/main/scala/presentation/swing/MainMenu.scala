package presentation
package swing

import java.awt.Font

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Button, GridPanel, Label, MainFrame, Orientation, Swing, SwingApplication}

private [swing] class MainMenu(fontSpecs: Font) extends MainFrame {

  private val manualEntry: FrameKit =  FrameKitFactory(fontSpecs, ManualEntry)

  private val uploadStatement: Button = new Button {
    text = "Upload Statement"
    font = fontSpecs
  }

  private val viewSummary: Button = new Button {
    text = "View Summary"
    font = fontSpecs
  }

  private val calcBudget: Button = new Button {
    text = "Calculate Budget"
    font = fontSpecs
  }

  title = "Personal Finance System"
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(2,2) {

      contents += uploadStatement
      contents += manualEntry.button
      contents += viewSummary
      contents += calcBudget

      border = Swing.EmptyBorder(30, 30, 30, 30)
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label {
        text = "£ $ €"
        font = new Font(fontSpecs.getFontName(),Font.BOLD,100)
      }
      border = Swing.EmptyBorder(30, 30, 30, 30)
    }
  }

  listenTo(uploadStatement,manualEntry.button,viewSummary,calcBudget)

  reactions += {
    case ButtonClicked(`manualEntry`) => {
      MainWindow.whatIsShowing.visible = false

    }
  }
}
