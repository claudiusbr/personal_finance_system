package personalfinance.presentation

import swing.SwingMediator
import play.PlayMediator

trait PresentationMediator extends SwingMediator with PlayMediator {
  def startup(): Unit
}
