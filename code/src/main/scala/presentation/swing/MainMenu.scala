package presentation
package swing

import java.awt.Font

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, GridPanel, Label, MainFrame, Orientation, Swing}

private [swing] class MainMenu(fontSpecs: Font) extends MainFrame {

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
    case ButtonClicked(`meBtn`) =>
      val other = MainWindow.whatIsShowing
      manualEntry.frame.location = other.location
      MainWindow.whatIsShowing = manualEntry.frame
      MainWindow.whatIsShowing.visible = true
      other.visible = false

    case ButtonClicked(`usBtn`) =>
      val other = MainWindow.whatIsShowing
      uploadStatement.frame.location = other.location
      MainWindow.whatIsShowing = uploadStatement.frame
      MainWindow.whatIsShowing.visible = true
      other.visible = false

    case ButtonClicked(`vsBtn`) =>
      val other = MainWindow.whatIsShowing
      viewSummary.frame.location = other.location
      MainWindow.whatIsShowing = viewSummary.frame
      MainWindow.whatIsShowing.visible = true
      other.visible = false

    case ButtonClicked(`cbBtn`) =>
      val other = MainWindow.whatIsShowing
      calcBudget.frame.location = MainWindow.whatIsShowing.location
      MainWindow.whatIsShowing = calcBudget.frame
      MainWindow.whatIsShowing.visible = true
      other.visible = false
  }
}
