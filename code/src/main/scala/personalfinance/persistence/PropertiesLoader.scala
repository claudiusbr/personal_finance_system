package personalfinance
package persistence

trait PropertiesLoader {
  def getProperty(p: String): String
}
