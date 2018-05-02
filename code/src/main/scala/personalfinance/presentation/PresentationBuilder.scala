package personalfinance
package presentation

/**
  * this builder object is responsible for using reflection to determine
  * which front-end package the application is currently used.
  * This will need to be specified in the `config.properties` file in the root directory
  * from which the application will be running
  */
object PresentationBuilder {

  def getPresentationAmbassador(frontEndChoice: String, mediator: PresentationMediator): PresentationMediator = {
    Class.forName(s"personalfinance.presentation.$frontEndChoice")
      .getConstructor(Class.forName("personalfinance.presentation.PresentationMediator"))
        .newInstance(mediator)
        .asInstanceOf[PresentationMediator]
  }
}