package presentation.swing

import scala.swing._
import scala.swing.event._
import Orientation.{Vertical,Horizontal}
import Swing.{VGlue,VStrut,HGlue,HStrut}

/**
  * this is the implementation of the interface for manual entry
  */
private[swing] case object ManualEntry extends KitName { val title = "Manual Entry" }
private[swing] class ManualEntry(fontSpecs: Font, main: MainMenu) extends OtherMenu(main) {

  // top box
  private val typeLabel = new Label("Type")
  private val typeDropDown = new ComboBox[String](Seq("Income","Expenditure"))
  private val typeBox = new BoxPanel(Vertical) {
    contents ++= Array(typeLabel,VGlue,typeDropDown)
  }

  //private val dateLabel = new Label("Date")
  //private val dateField = new TextField {columns = 15}
  //private val dateBox = new BoxPanel(Vertical) {
  //  contents ++= Array(dateLabel,VGlue,dateField)
  //}

  private val dateBox = getDateBox()

  private val totalLabel = new Label("Total")
  private val totalField = new TextField {columns = 7}
  private val totalBox = new BoxPanel(Vertical) {
    contents ++= Array(totalLabel, VGlue, totalField)
  }

  private val currencyLabel = new Label("Currency")
  private val currencyDropDown = new ComboBox[String](Seq("GBP"))
  private val currencyBox = new BoxPanel(Vertical) {
    contents ++= Array(currencyLabel,VGlue,currencyDropDown)
  }

  private val topBox = new BoxPanel(Horizontal) {
    contents += HGlue
    contents += typeBox
    contents += HGlue
    contents += dateBox
    contents += HGlue
    contents += totalBox
    contents += HGlue
    contents += currencyBox
    contents += HGlue
    border = Swing.EmptyBorder(10,10,10,10)
  }

  // upper middle box
  private val descriptionLabel = new Label("Description")
  private val descriptionField = new TextField {columns = main.WindowWidth-20}

  private val upperMiddleBox = new BoxPanel(Vertical) {
    contents += descriptionLabel
    contents += descriptionField
    border = Swing.EmptyBorder(10,10,10,10)
  }

  // lower middle box
  private val breakDownLabel = new Label("Breakdown")

  private val categoryLabel = new Label("Category")
  private val categoryField = new TextField(columns = 25)
  private val categoryBox = new BoxPanel(Vertical) {
    contents ++= Array(categoryLabel,VGlue,categoryField)
  }

  private val percentLabel = new Label("%")
  private val percentField = new TextField(columns = 5)
  private val percentBox = new BoxPanel(Vertical) {
    contents ++= Array(percentLabel,VGlue,percentField)
  }

  private val amountLabel = new Label("Amount")
  private val amountField = new TextField(columns = 7)
  private val amountBox = new BoxPanel(Vertical) {
    contents ++= Array(amountLabel,VGlue,amountField)
  }

  private val newLineButton = new Button("New Line")

  private val breakDownBox = new BoxPanel(Horizontal) {
    contents ++= Array(categoryBox,percentBox,amountBox)
    border = Swing.EtchedBorder
  }

  private val lowerMiddleBox = new BoxPanel(Vertical) {
    contents += new BoxPanel(Vertical) {
      contents += breakDownLabel
      contents += breakDownBox
      contents += newLineButton
      border = Swing.EmptyBorder(10,10,10,10)
    }
    border = Swing.EtchedBorder
  }

  // bottom box
  private val bottomBox = navigationBox

  private val allBoxes = Array(topBox,upperMiddleBox,lowerMiddleBox,bottomBox)

  allBoxes.foreach { setMaxHeight }

  title = ManualEntry.title
  contents = new BoxPanel(Vertical) {
    contents ++= allBoxes
    border = Swing.EmptyBorder(30,30,30,30)
  }

}
