package presentation
package swing

import java.awt.Font

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, GridPanel, Label, MainFrame, Orientation, Swing}

private [swing] class MainMenu(fontSpecs: Font) extends MainFrame {

  private val (manualEntry,uploadStatement,viewSummary,calcBudget) = (
    FrameKitFactory(fontSpecs, ManualEntry),
    FrameKitFactory(fontSpecs, UploadStatement),
    FrameKitFactory(fontSpecs, ViewSummary),
    FrameKitFactory(fontSpecs, CalculateBudget)
  )

  title = "Personal Finance System"
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(2,2) {

      contents += uploadStatement.button
      contents += manualEntry.button
      contents += viewSummary.button
      contents += calcBudget.button

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

  listenTo(uploadStatement.button,manualEntry.button,viewSummary.button,calcBudget.button)

  reactions += {
    case ButtonClicked(`manualEntry`) => {
      MainWindow.whatIsShowing.visible = false

    }
  }
}
