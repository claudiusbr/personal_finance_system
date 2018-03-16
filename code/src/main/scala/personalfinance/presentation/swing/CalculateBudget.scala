package personalfinance
package presentation
package swing

import java.awt.Font
import java.io.File

import scala.swing._
import scala.swing.event._
import Orientation.{Vertical,Horizontal}
import Swing.{VGlue,VStrut,HGlue,HStrut}

private[swing] case object CalculateBudget extends KitName { val title = "Calculate Budget" }
private[swing] class CalculateBudget(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {
  title = CalculateBudget.title
  contents = navigationBox
}

