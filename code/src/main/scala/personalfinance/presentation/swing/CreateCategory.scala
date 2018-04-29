package personalfinance.presentation.swing

import scala.swing._
import scala.swing.event._
import scala.swing.Swing.HGlue
import java.awt.Font

private[swing] class CreateCategory(entryType: String, date: String, description: String,
                                    amount: String, others: Seq[(String,String,String,String)],
                                    fontSpecs: Font, main: MainMenu, mediator: SwingMediator)
  extends MainFrame {

  private val cancelBtn = new Button("Cancel")
  private val okBtn = new Button("Submit")

  private val me: CreateCategory = this

  private val navigationBox: BoxPanel =
    new BoxPanel(Orientation.Horizontal) {
      contents += cancelBtn
      contents += Swing.HStrut(3)
      contents += okBtn
    }

  private val messenger = new Label("")

  private val instructionsLabel = new Label("Choose or create a category for the entry below:")

  private val categoriesDropDown =
    new ComboBox[String](Seq("") ++ mediator.getAllCategoryNames())
  private val categoryField = new TextField("")

  private val entryTypeLabel = new Label(entryType)
  private val descriptionLabel = new Label(s"Description: $description")
  private val dateCreatedLabel = new Label(s"Date Created: $date")

  private val patternLabel = new Label("New pattern for this category")
  private val patternField = new TextField(description)

  private val amountLabel = new Label(s"Amount: $amount")

  private val box = new BoxPanel(Orientation.Vertical) {
    contents += instructionsLabel
    contents += Swing.VStrut(3)
    contents += categoryField
    contents += Swing.VStrut(3)
    contents += categoriesDropDown
    contents += Swing.VStrut(8)
    contents += new BoxPanel(Orientation.Horizontal) {
      contents ++= Seq(entryTypeLabel, HGlue, dateCreatedLabel)
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents ++= Seq(descriptionLabel, HGlue, amountLabel)
    }
    contents += new BoxPanel(Orientation.Vertical) {
      contents ++= Seq(patternLabel, Swing.VStrut(2),
        patternField,Swing.VStrut(2))
    }

    contents += messenger

    border = Swing.EmptyBorder(10, 10, 10, 10)
  }


  /**
    * an aid to set the maximum height of text fields and other components
    * @param comp a Component to set maximum height
    */
  private def setMaxHeight(comp: Component): Unit =
    comp.maximumSize = new Dimension(Short.MaxValue, comp.preferredSize.height)


  setMaxHeight(box)

  contents = new BoxPanel(Orientation.Vertical) {
    contents ++= Seq(box,Swing.VStrut(5),navigationBox)
    border = Swing.EmptyBorder(30, 30, 30, 30)
  }

  listenTo(cancelBtn,okBtn, categoriesDropDown.selection, this)

  reactions += {
    case SelectionChanged(`categoriesDropDown`) => {
      println(s"selection changed to ${categoriesDropDown.selection.item}")
      categoryField.text = categoriesDropDown.selection.item
    }

    case ButtonClicked(`cancelBtn`) => {
      main.location = this.location
      main.visible = true
      this.visible = false
   }

    case WindowActivated(`me`) => this.size = main.size

    case ButtonClicked(`okBtn`) => {
      categoryField.text match {
        case "" => messenger.text = "Category cannot be empty"
        case cat if cat.length <= 1 => messenger.text = "Category names should " +
          "be at least 2 characters long."
        case _ => {
          main.visible = true
          this.visible = false
          mediator.classifyWithNewCategory(
            categoryField.text,
            patternField.text,
            Seq((entryType,date,description,amount)) ++ others
          )
        }
      }
    }
  }

}
