package personalfinance.presentation.swing

import scala.swing.Button

/**
  * a button with a field of type KitName -- will be used to identify
  * which button was pressed in the MainWindow
  * @param kitName the the name of the FrameKit to which the button belongs
  * @param text0 the button text
  */
class MainButton(val kitName: KitName, text0: String) extends Button(text0)
