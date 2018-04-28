package personalfinance.presentation.swing

import java.awt.Font

/**
  * this object is responsible for returning the right menu to the main menu
  */
object FrameKitFactory {
  def apply(fontSpecs: Font, kitName: KitName, main: MainMenu, mediator: SwingMediator): FrameKit =
    kitName match {

    case ManualEntry => new FrameKit(
      new ManualEntry(fontSpecs, main, mediator),
      new MainButton(ManualEntry,ManualEntry.title) {
        font = fontSpecs
      }
    )

    case UploadStatement => new FrameKit(
      new UploadStatement(fontSpecs, main, mediator),
      new MainButton(UploadStatement,UploadStatement.title) {
        font = fontSpecs
      }
    )

    case ViewSummary => new FrameKit(
      new ViewSummary(fontSpecs, main, mediator),
      new MainButton(ViewSummary,ViewSummary.title) {
        font = fontSpecs
      }
    )

    case CalculateBudget => new FrameKit(
      new CalculateBudget(fontSpecs, main, mediator),
      new MainButton(CalculateBudget,CalculateBudget.title) {
        font = fontSpecs
      }
    )
  }
}
