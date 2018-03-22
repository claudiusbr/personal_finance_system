package personalfinance
package presentation
package swing
package frames

import java.awt.Font

/**
  * this object is responsible for returning the right menu to the main menu
  */
object FrameKitFactory {
  def apply(fontSpecs: Font, kitName: KitName, main: MainMenu): FrameKit =
    kitName match {

    case ManualEntry => new FrameKit(
      new ManualEntry(fontSpecs, main),
      new MainButton(ManualEntry,ManualEntry.title) {
        font = fontSpecs
      }
    )

    case UploadStatement => new FrameKit(
      new UploadStatement(fontSpecs, main),
      new MainButton(UploadStatement,UploadStatement.title) {
        font = fontSpecs
      }
    )

    case ViewSummary => new FrameKit(
      new ViewSummary(fontSpecs, main),
      new MainButton(ViewSummary,ViewSummary.title) {
        font = fontSpecs
      }
    )

    case CalculateBudget => new FrameKit(
      new CalculateBudget(fontSpecs, main),
      new MainButton(CalculateBudget,CalculateBudget.title) {
        font = fontSpecs
      }
    )
  }
}
