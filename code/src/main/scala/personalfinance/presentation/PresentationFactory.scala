package personalfinance
package presentation

object PresentationFactory {

  def getPresentationAmbassador(frontEndChoice: String, mediator: PresentationMediator): PresentationMediator = {
    Class.forName(s"personalfinance.presentation.$frontEndChoice")
      .getConstructor(Class.forName("personalfinance.presentation.PresentationMediator"))
        .newInstance(mediator)
        .asInstanceOf[PresentationMediator]
  }
}