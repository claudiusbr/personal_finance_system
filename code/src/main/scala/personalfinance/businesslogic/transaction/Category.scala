package personalfinance
package businesslogic
package transaction

/**
  * Creates Categories, which are the implementation of Fowler's Account pattern.
  *
  * @param name the class name
  * @param entries a list of Entry types
  */
case class Category private[transaction](name: String, entries: List[Entry], patterns: Patterns, id: Int = 0) {
  def this(name: String) = this(name,List[Entry](),Patterns(List[String]()))
  def this(name: String, id: Int) = this(name,List[Entry](),Patterns(List[String]()),id)
  def this(name: String, patterns: Patterns) = this(name,List[Entry](),patterns)
  def this(name: String, patterns: Patterns, id: Int) = this(name,List[Entry](),patterns, id)
}
