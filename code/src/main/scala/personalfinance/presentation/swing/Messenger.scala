package personalfinance.presentation.swing

import scala.swing.{BoxPanel, Label, Orientation, Swing}

private[swing] object Messenger {

  val messenger = new Label("")

  val messengerBox: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    contents ++= Seq(messenger,Swing.HGlue)
  }

  def warnUser(message: String): Unit = {
    /*
    TODO: fix messenger
    messenger.text = ""
    messenger.foreground = Colour.RED
    messenger.text = message
    */
  }

  def informUser(message: String): Unit = {
    /*
    TODO: fix messenger
    messenger.text = ""
    messenger.foreground = Colour.BLACK
    messenger.text = message
     */
  }

  def sendConfirmationMessage(message: String): Unit = {
    /*
    TODO: fix messenger
    messenger.text = ""
    messenger.foreground = Colour.BLUE
    messenger.text = message
    */
  }

  def currentMessageToUser: String = messenger.text

}
