package presentation
package swing

import java.awt.Font
import javax.swing.ImageIcon

import scala.swing.{BoxPanel, Button, Dimension, GridPanel, Label, MainFrame, Orientation, SimpleSwingApplication, Swing}

// TODO: implement logging capabilities

object MainMenu extends SimpleSwingApplication {
  def top: MainFrame = new MainFrame {
    private val verdana: Font = new Font("Verdana", Font.BOLD, 20)
    title = "Personal Finance System"
    contents =
      new BoxPanel(Orientation.Vertical) {
        contents += new GridPanel(2,2) {

          contents += new Button {
            text = "Upload Statement"
            font = verdana
          }

          contents += new Button {
            text = "Manual Entry"
            font = verdana
          }

          contents += new Button {
            text = "View Summary"
            font = verdana
          }

          contents += new Button {
            text = "Calculate Budget"
            font = verdana
          }

          border = Swing.EmptyBorder(30, 30, 30, 30)
        }

        contents += new BoxPanel(Orientation.Horizontal) {
          contents += new Label {
            text = "£ $ €"
            font = new Font("verdana",Font.BOLD,100)
          }
          border = Swing.EmptyBorder(30, 30, 30, 30)
        }
      }
  }
}
