package personalfinance
package presentation
package swing
package frames

import java.awt.Font
import java.io.File

import scala.swing._
import Orientation.{Horizontal, Vertical}
import Swing.HGlue
import scala.swing.event.ButtonClicked

private[swing] case object UploadStatement extends KitName { val title = "Upload Statement" }
private[swing] class UploadStatement(fontSpecs: Font, main: MainMenu, mediator: SwingMediator)
  extends OtherMenu(main) {

  private val fileLabel = new Label("CSV Path")
  private val fileFieldText = "(enter absolute file path or click 'Open')"
  private val fileField = new TextField(fileFieldText) {
    columns = main.WindowWidth - 20
  }
  private val fileButton = Button("Open")(fileField.text = getFileName(
    new FileChooser(new File("."))
  ))
  private val fileBox = new BoxPanel(Horizontal) {
    contents ++= Array(fileField, HGlue, fileButton)
  }

  private val uploadButton = new Button("Upload")
  override protected val navigationBox: BoxPanel = new BoxPanel(Horizontal) {
    contents ++= Array(cancelBtn,uploadButton)
  }

  private val findFileBox = new BoxPanel(Vertical) {
    contents += fileLabel
    contents += fileBox
    contents += navigationBox
    border = Swing.EtchedBorder
  }

  private val messageLabel =
    new Label("Please choose the csv version of the statement," +
      " then press 'Upload'")

  private val uploadStatementBox = new BoxPanel(Vertical) {
    contents += findFileBox
    contents += messageLabel
  }


  private def getFileName(opener: FileChooser): String = {
    if (opener.showOpenDialog(null) == FileChooser.Result.Approve)
      opener.selectedFile.getAbsolutePath
    else ""
  }

  setMaxHeight(fileField)

  title = UploadStatement.title
  contents = uploadStatementBox

  fileButton.requestFocus()

  listenTo(okBtn)

  reactions += {
    case ButtonClicked(`okBtn`) => mediator.uploadStatement(fileField.text)
  }
}
