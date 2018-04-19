package personalfinance
package presentation

import swing.frames.MainWindow

private[presentation] case class Swing() extends PresentationBridge {
  private val mainWindow = MainWindow

  override def startup(): Unit = mainWindow.startup()
  override def quit(): Unit = mainWindow.quit()
}
