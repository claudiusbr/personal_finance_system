package personalfinance.presentation

import swing.SwingMediator
import play.PlayMediator

/**
  * this trait needs to extend all mediators for all front-end implementations
  * for the presentation layer. Any class external to the presentation
  * package will need to implement this trait, and then be passed to the respective
  * ambassador of the class chosen at runtime with the aid of the
  * PresentationFactory
  */
trait PresentationMediator extends PlayMediator with SwingMediator {
  def startup(): Unit
}
