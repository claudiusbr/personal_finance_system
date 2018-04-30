package personalfinance.presentation.swing

import java.awt.Font

import scala.swing.{BoxPanel, ButtonGroup, Orientation, Panel, RadioButton, Swing}
import scala.swing.event.ButtonClicked

private[swing] case object CalculateBudget extends KitName {
  val title = "Calculate Budget"

  private val monthlyButton = new RadioButton("Monthly")
  private val yearlyButton = new RadioButton("Yearly")
  monthlyButton.selected = true
  val statusGroup = new ButtonGroup(monthlyButton,yearlyButton)

  private val buttons: Panel = new BoxPanel(Orientation.Horizontal) {
    contents += monthlyButton
    contents += Swing.HStrut(10)
    contents += yearlyButton
  }
}
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu, mediator: SwingMediator,
                                     budget: Panel = CalculateBudget.buttons)
  extends OtherMenu(main) {

  title = CalculateBudget.title
  contents = new BoxPanel(Orientation.Vertical) {
    contents += budget
    contents += navigationBox
  }

  reactions += {
    case ButtonClicked(`okBtn`) => {
      this.visible = false
      main.visible = true
      if (CalculateBudget.monthlyButton.selected) mediator.requestMonthlyBudget()
      else if (CalculateBudget.yearlyButton.selected) mediator.requestYearlyBudget()
    }
  }
}