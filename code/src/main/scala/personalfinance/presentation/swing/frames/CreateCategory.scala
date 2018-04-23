package personalfinance
package presentation
package swing
package frames


import java.awt.Font

private[swing] case object CreateCategory extends KitName { val title = "Upload Statement" }
private[swing] class CreateCategory(fontSpecs: Font, main: MainMenu, mediator: SwingMediator)
  extends OtherMenu(main) {

}
