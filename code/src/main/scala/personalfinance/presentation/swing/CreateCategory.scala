package personalfinance.presentation.swing

import java.awt.Font

private[swing] case object CreateCategory extends KitName { val title = "Upload Statement" }
private[swing] class CreateCategory(fontSpecs: Font, main: MainMenu, mediator: SwingMediator)
  extends OtherMenu(main) {

}
