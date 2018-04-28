package personalfinance.presentation.swing


import scala.swing._
import scala.swing.event._

import java.awt.Font

private[swing] class CreateCategory(entryType: String, date: String, description: String,
                                    amount: String, fontSpecs: Font, main: MainMenu,
                                    mediator: SwingMediator) extends MainFrame {

  protected val cancelBtn = new Button("Cancel")
  protected val okBtn = new Button("Submit")

  protected val me: CreateCategory = this

  protected val navigationBox: BoxPanel =
    new BoxPanel(Orientation.Horizontal) {
      contents += cancelBtn
      contents += Swing.HStrut(3)
      contents += okBtn
    }

  private val entryTypeLabel = new Label(entryType)
  private val descriptionLabel = new Label(s"Description: $description")
  private val dateCreatedLabel = new Label(s"Date Created: $date")

  private val patternLabel = new Label("New pattern for this category")
  private val patternField = new TextField(description)

  private val amountLabel = new Label(s"Amount: $amount")

  private val searchCategoryPopUpBtn = new Button("Choose Category from Existing")

  /**
    * an aid to set the maximum height of text fields and other components
    * @param comp a Component to set maximum height
    */
  protected def setMaxHeight(comp: Component): Unit =
    comp.maximumSize = new Dimension(Short.MaxValue, comp.preferredSize.height)

  listenTo(cancelBtn,okBtn, this)

  reactions += {
    case ButtonClicked(`cancelBtn`) =>
      // TODO: show pop up saying that pressing
      main.location = this.location
      main.visible = true
      this.visible = false

    case WindowActivated(`me`) => this.size = main.size
  }

}
