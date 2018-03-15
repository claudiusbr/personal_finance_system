package presentation
package swing

import java.awt.Font

import scala.swing.{Button, Frame}


/**
  * This class is just a bean to store a button object
  * and a frame which gets triggered by it
  * @param frame the frame to be loaded
  * @param button the button which loads the frame
  */
class FrameKit(val frame: Frame, val button: Button)

/**
  * this object is responsible for returning the right menu to the main menu
  */
object FrameKitFactory {
  def apply(fontSpecs: Font, kitName: KitName): FrameKit = kitName match {

    case ManualEntry => new FrameKit(
      new ManualEntry(fontSpecs),
      new Button {
        text = "Manual Entry"
        font = fontSpecs
      }
    )

    case UploadStatement => new FrameKit(
      new UploadStatement(fontSpecs),
      new Button {
        text = "Upload Statement"
        font = fontSpecs
      }
    )

    case ViewSummary => new FrameKit(
      new ViewSummary(fontSpecs),
      new Button {
        text = "View Summary"
        font = fontSpecs
      }
    )

    case CalculateBudget => new FrameKit(
      new CalculateBudget(fontSpecs),
      new Button {
        text = "Calculate Budget"
        font = fontSpecs
      }
    )

  }
}
