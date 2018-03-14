package businesslogic
package transaction

/**
  * Creates Categories, which are the implementation of Fowler's Account pattern.
  * @param name the class name
  * @param entries a list of Entry types
  */
case class Category private[transaction] (name: String, entries: List[Entry]) {
  def this(name: String) = this(name,List[Entry]())
}
