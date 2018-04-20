package personalfinance
package presentation
package swing
package frames

import java.awt.Font

import scala.swing.event.ButtonClicked

private[swing] case object CalculateBudget extends KitName {
  val title = "Calculate Budget"
}
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu,
                                     mediator: SwingMediator)
  extends OtherMenu(main) {

  title = CalculateBudget.title
  contents = navigationBox

  reactions += {
    case ButtonClicked(`okBtn`) => mediator.calculateBudget()
  }
}

