package personalfinance.presentation.swing

import java.awt.Font

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Dimension, GridPanel, Label, MainFrame, Orientation, Swing}

private [swing] class MainMenu(fontSpecs: Font, mediator: SwingMediator) extends MainFrame {

  private[swing] val WindowHeight = 390
  private[swing] val WindowWidth = 710
  preferredSize = new Dimension(WindowWidth,WindowHeight)

  private val (manualEntry,uploadStatement,viewSummary,calcBudget) = (
    FrameKitFactory(fontSpecs, ManualEntry, this, mediator),
    FrameKitFactory(fontSpecs, UploadStatement, this, mediator),
    FrameKitFactory(fontSpecs, ViewSummary, this, mediator),
    FrameKitFactory(fontSpecs, CalculateBudget, this, mediator)
  )

  private val (usBtn,meBtn,vsBtn,cbBtn) = (
    uploadStatement.button,manualEntry.button,
    viewSummary.button,calcBudget.button
  )

  def createCategory(entries: Seq[(String,String,String,String)]): Unit = {
    val toBeCategorised = entries.head
    val toSendBack = entries.tail
    val entryType = toBeCategorised._1
    val date = toBeCategorised._2
    val description = toBeCategorised._3
    val amount = toBeCategorised._4
    val createCategoryFrame = new CreateCategory(
      entryType, date, description, amount, toSendBack, fontSpecs, this, mediator
    )
    createCategoryFrame.location = this.location
    createCategoryFrame.visible = true
    this.visible = false
  }

  private def mainButtonAction(b: MainButton): Unit = {
    val frameKit: FrameKit = b.kitName match {
      case ManualEntry => manualEntry
      case UploadStatement => uploadStatement
      case ViewSummary => viewSummary
      case CalculateBudget => calcBudget
    }
    frameKit.frame.location = this.location
    frameKit.frame.visible = true
    this.visible = false
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
