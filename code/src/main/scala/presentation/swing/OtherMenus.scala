package presentation
package swing
import java.awt.Font

import scala.swing._

sealed trait KitName

private[swing] case object ManualEntry extends KitName
private[swing] class ManualEntry(fontSpecs: Font) extends MainFrame {
  title = "Manual Entry"
}


private[swing] case object UploadStatement extends KitName
private[swing] class UploadStatement(fontSpecs: Font) extends MainFrame {
  title = "Upload Statement"
}


private[swing] case object ViewSummary extends KitName
private[swing] class ViewSummary(fontSpecs: Font) extends MainFrame {
  title = "View Summary"
}


private[swing] case object CalculateBudget extends KitName
private[swing] class CalculateBudget(fontSpecs: Font) extends MainFrame {
  title = "Calculate Budget"
}

