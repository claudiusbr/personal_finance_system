package personalfinance

trait Mediator {
  protected val propertiesLoader = new PropertiesLoader("config.properties")
  protected val privateLoader = new PropertiesLoader("private.properties")
}
