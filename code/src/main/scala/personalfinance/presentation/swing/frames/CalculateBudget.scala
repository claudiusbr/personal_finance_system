package personalfinance
package presentation
package swing
package frames

import java.awt.Font

private[swing] case object CalculateBudget extends KitName {
  val title = "Calculate Budget"
}
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {
  title = CalculateBudget.title
  contents = navigationBox
}

