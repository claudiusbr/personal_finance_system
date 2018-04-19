package personalfinance

import java.io.FileInputStream
import java.util.Properties

/**
  * This class will be used to load properties onto the system
  * @param fileName the name of the properties file
  */
private[personalfinance] class PropertiesLoader(fileName: String) extends
  persistence.PropertiesLoader with presentation.PropertiesLoader {

  private val props: Properties = new Properties()

  props.load(new FileInputStream(fileName))

  def getProperty(p: String): String = props.getProperty(p)
}