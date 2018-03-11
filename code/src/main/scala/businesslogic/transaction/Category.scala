package businesslogic
package transaction

/**
  * Creates Categories, which are the implementation of Fowler's Account pattern.
  * @param name the class name
  * @param entries a list of Entry types
  */
class Category private[transaction] (val name: String, val entries: List[Entry]) {
  def this(name: String) = this(name,List[Entry]())
}
