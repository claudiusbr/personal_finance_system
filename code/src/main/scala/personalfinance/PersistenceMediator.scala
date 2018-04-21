package personalfinance

import personalfinance.persistence.PersistenceBridge

/**
  * This object is responsible for handling the interaction between the
  * application logic and the persistence layer
  */
object PersistenceMediator extends Mediator {
  private val persistenceBridge =
    new PersistenceBridge(propertiesLoader,privateLoader)

  def startup(): Unit = persistenceBridge.connect()

  def quit(): Unit = persistenceBridge.closeConnection()
}
