package presentation
package swing

import scala.swing.{MainFrame, SimpleSwingApplication, Button}

object MainMenu extends SimpleSwingApplication {
  def top: MainFrame = new MainFrame {
    title = "Personal Finance System"
    contents = new Button {
      text = "Hey!"
    }
  }
}
