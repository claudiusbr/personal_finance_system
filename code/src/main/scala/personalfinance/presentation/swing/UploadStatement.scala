package personalfinance.presentation.swing

import java.awt.Font
import java.io.File

import java.nio.file.{Files,Paths}

import scala.swing.Orientation.{Horizontal, Vertical}
import scala.swing.Swing.HGlue
import scala.swing._
import scala.swing.event.ButtonClicked

private[swing] case object UploadStatement extends KitName { val title = "Upload Statement" }
private[swing] class UploadStatement(fontSpecs: Font, main: MainMenu, mediator: SwingMediator)
  extends OtherMenu(main) {

  private val fileLabel = new Label("CSV Path")
  private val fileFieldText = ""

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
    contents += Messenger.messengerBox
    contents += new BoxPanel(Orientation.Vertical) {
      contents += navigationBox
    }
    border = Swing.EtchedBorder
  }

  private val uploadStatementBox = new BoxPanel(Vertical) {
    contents += findFileBox
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

  listenTo(uploadButton)

  reactions += {
    case ButtonClicked(`uploadButton`) => {
      if (fileField.text != "" && Files.exists(Paths.get(fileField.text))) {
        Messenger.informUser("Processing statement entries. Please wait.")
        mediator.uploadStatement(fileField.text)
        main.location = this.location
        this.visible = false
        //main.visible = true
      } else {
        Messenger.warnUser(s"File ${fileField.text} not found.")
      }
    }
  }
}
