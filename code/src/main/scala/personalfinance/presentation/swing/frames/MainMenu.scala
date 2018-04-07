package personalfinance
package presentation
package swing
package frames

import java.awt.Font

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Dimension, GridPanel, Label, MainFrame, Orientation, Swing}

private [swing] class MainMenu(fontSpecs: Font) extends MainFrame {

  private[swing] val WindowHeight = 390
  private[swing] val WindowWidth = 710
  preferredSize = new Dimension(WindowWidth,WindowHeight)

  private val (manualEntry,uploadStatement,viewSummary,calcBudget) = (
    FrameKitFactory(fontSpecs, ManualEntry, this),
    FrameKitFactory(fontSpecs, UploadStatement, this),
    FrameKitFactory(fontSpecs, ViewSummary, this),
    FrameKitFactory(fontSpecs, CalculateBudget, this)
  )

  val (usBtn,meBtn,vsBtn,cbBtn) = (
    uploadStatement.button,manualEntry.button,
    viewSummary.button,calcBudget.button
  )

  private def mainButtonAction(b: MainButton): Unit = {
    val frameKit: FrameKit = b.kitName match {
      case ManualEntry => manualEntry
      case UploadStatement => uploadStatement
      case ViewSummary => viewSummary
      case CalculateBudget => calcBudget

    }
    val other = MainWindow.whatIsShowing
    frameKit.frame.location = other.location
    MainWindow.whatIsShowing = frameKit.frame
    MainWindow.whatIsShowing.visible = true
    other.visible = false
  }

  title = "Personal Finance System"
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(2,2) {

      contents += usBtn
      contents += meBtn
      contents += vsBtn
      contents += cbBtn

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

  listenTo(usBtn,meBtn,vsBtn,cbBtn)

  reactions += {
    case ButtonClicked(b: MainButton) => mainButtonAction(b)
  }
}