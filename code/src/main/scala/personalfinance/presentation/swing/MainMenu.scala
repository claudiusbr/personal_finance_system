package personalfinance.presentation.swing

import java.awt.Font

import java.util.{Locale, Currency}
import java.text.NumberFormat

import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Dialog, Dimension, GridPanel, Label, MainFrame, Orientation, Swing}

private [swing] class MainMenu(fontSpecs: Font, mediator: SwingMediator) extends MainFrame {


  private val gbpFormatter = NumberFormat.getCurrencyInstance



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

  def displayBudget(budget: Seq[(String, Double, Double)]): Unit = {
    val head =
      "<html><table style='font-family:monospace; width:400px;'><tr>" +
        "<th>Category</th><th>Budgeted Amount</th><th>Budgeted % of Income</th></tr>"

    val body =
      budget.foldLeft("")((str,catAmtAndPerc) => {
        str + s"<tr><td>${catAmtAndPerc._1}</td>" +
          s"<td>${gbpFormatter.format(catAmtAndPerc._2)}</td>" +
          f"<td>${catAmtAndPerc._3}%2.2f"+"%</td>" +
          "</tr>"
      })

    val foot = "</table></html>"

    Dialog.showMessage(message=head+body+foot)
  }


  def displaySummary(from: String,to: String, summary: Seq[(String,Double)]): Unit = {
    val head =
      "<html><table style='font-family:monospace; width:400px;'><tr>" +
        "<th>Category</th><th>Amount</th></tr>"

    val (headAndbody: String, sum: Double) = summary.reverse.foldLeft((head,0.0))((strSum,catAndAmt) => {
      if (catAndAmt._1 == "Bank") strSum
      else
        (strSum._1 + s"<tr><td>${catAndAmt._1}</td><td>${gbpFormatter.format(catAndAmt._2)}</td></tr>",
          strSum._2 + catAndAmt._2)
    })

    val foot = s"<tr><th>Balance</th><th>$sum</th></tr></table></html>"

    Dialog.showMessage(message=headAndbody+foot)
  }

  private def mainButtonAction(b: MainButton): Unit = {
    val frameKit: FrameKit = b.kitName match {
      case ManualEntry => manualEntry
      case UploadStatement => uploadStatement
      case ViewSummary => viewSummary
      case CalculateBudget => calcBudget
    }
    Messenger.informUser("")
    frameKit.frame.location = this.location
    frameKit.frame.visible = true
    Messenger.informUser(frameKit.frameMessage)
    this.visible = false
  }

  gbpFormatter.setCurrency(Currency.getInstance(new Locale("en","GB")))

  title = "Personal Finance System"
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new GridPanel(2,2) {

      contents += usBtn
      contents += meBtn
      contents += vsBtn
      contents += cbBtn

      border = Swing.EmptyBorder(30, 30, 30, 30)
    }

    contents += new BoxPanel(Orientation.Vertical) {
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Messenger.messengerBox
        contents += Swing.HGlue
        border = Swing.EmptyBorder(30, 30, 30, 30)
      }

      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label {
          text = "£ $ €"
          font = new Font(fontSpecs.getFontName(), Font.BOLD, 100)
        }
        border = Swing.EmptyBorder(30, 30, 30, 30)
      }
    }
  }

  listenTo(usBtn,meBtn,vsBtn,cbBtn)

  reactions += {
    case ButtonClicked(b: MainButton) => mainButtonAction(b)
  }
}
