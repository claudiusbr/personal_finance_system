package personalfinance.presentation.swing

import java.awt.Font

import scala.swing.Orientation.{Horizontal, Vertical}
import scala.swing.Swing.VGlue
import scala.swing._
import scala.swing.event.ButtonClicked

private[swing] case object ViewSummary extends KitName { val title = "View Summary" }
private[swing] class ViewSummary(fontSpecs: Font, main: MainMenu,
                                 mediator: SwingMediator) extends OtherMenu(main) {
  private val fromLabel = new Label("From")
  private val fromField = new TextField {columns = 15}
  private val fromBox = new BoxPanel(Vertical) {
    contents ++= Array(fromLabel,VGlue,fromField)
  }

  private val toLabel = new Label("To")
  private val toField = new TextField {columns = 15}
  private val toBox = new BoxPanel(Vertical) {
    contents ++= Array(toLabel,VGlue,toField)
  }

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

  reactions += {
    case ButtonClicked(`okBtn`) =>
      mediator.viewSummary(fromField.text,toField.text)
  }
}

