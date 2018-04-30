package personalfinance.presentation.swing

import java.awt.Font

import scala.swing.{BoxPanel, Orientation, Panel}
import scala.swing.event.ButtonClicked

private[swing] case object CalculateBudget extends KitName {
  val title = "Calculate Budget"
}
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu, mediator: SwingMediator,
                                     budget: Panel = new BoxPanel(Orientation.Vertical))
  extends OtherMenu(main) {

  title = CalculateBudget.title
  contents = new BoxPanel(Orientation.Vertical) {
    contents += budget
    contents += navigationBox
  }

  reactions += {
    case ButtonClicked(`okBtn`) => Messenger.informUser("Button pressed")
  }
}