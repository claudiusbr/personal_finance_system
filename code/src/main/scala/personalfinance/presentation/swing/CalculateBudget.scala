package personalfinance.presentation.swing

import java.awt.Font

import scala.swing.{BoxPanel, Orientation}
import scala.swing.event.ButtonClicked

private[swing] case object CalculateBudget extends KitName {
  val title = "Calculate Budget"
}
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu,
                                     mediator: SwingMediator)
  extends OtherMenu(main) {

  title = CalculateBudget.title
  contents = new BoxPanel(Orientation.Vertical) {
    contents += navigationBox
  }

  reactions += {
    case ButtonClicked(`okBtn`) => Messenger.informUser("Button pressed")
  }
}

