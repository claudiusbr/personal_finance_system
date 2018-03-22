package personalfinance
package presentation
package swing
package frames

import scala.swing.Frame

/**
  * This class is just a bean to store a button object
  * and a frame which gets triggered by it
  * @param frame the frame to be loaded
  * @param button the button which loads the frame
  */
class FrameKit(val frame: Frame, val button: MainButton)

