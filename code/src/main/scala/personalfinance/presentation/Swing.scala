package personalfinance
package presentation

import swing.frames.MainWindow

private[presentation] case class Swing() extends PresentationBridge {
  private val swingMenus = MainWindow
  override def startup(): Unit = swingMenus.startup()

  override def quit(): Unit = swingMenus.quit()
}
