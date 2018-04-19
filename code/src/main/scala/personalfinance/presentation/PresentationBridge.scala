package personalfinance
package presentation

private [presentation] trait PresentationBridge {
  def startup(): Unit
  def quit(): Unit
}