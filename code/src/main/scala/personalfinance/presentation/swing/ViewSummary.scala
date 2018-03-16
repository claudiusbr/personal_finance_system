package personalfinance
package presentation
package swing

import java.awt.Font


import scala.swing._
import scala.swing.event._
import Orientation.{Horizontal, Vertical}
import Swing.{HGlue, HStrut, VGlue, VStrut}

private[swing] case object ViewSummary extends KitName { val title = "View Summary" }
private[swing] class ViewSummary(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {
  private val fromBox = getDateBox("From")
  private val toBox = getDateBox("To")

  private val topBox = new BoxPanel(Horizontal) {
    contents ++= Array(fromBox,Swing.HStrut(3),toBox)
    border = Swing.EmptyBorder(10)
  }


  private val catSearchLabel = new Label("Category (optional):")
  private val catSearchField = new TextField(
    "(start typing to initiate search)"
  )
  private val middleBox = new BoxPanel(Vertical) {
    contents ++= Array(catSearchLabel,Swing.HStrut(3),catSearchField)
  }

  Array(topBox,middleBox).foreach {setMaxHeight}

  title = ViewSummary.title
  contents = new BoxPanel(Vertical) {
    contents += topBox
    contents += middleBox
    contents += navigationBox
    border = Swing.EmptyBorder(30)
  }
}

