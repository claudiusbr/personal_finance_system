package personalfinance
package businesslogic
package transaction

/**
  * Creates Categories, which are the implementation of Fowler's Account pattern.
  *
  * @param name the class name
  * @param entries a list of Entry types
  */
case class Category private[transaction](name: String,
                                         entries: List[Entry] = List[Entry](),
                                         patterns: Patterns = Patterns(List[String]()),
                                         id: Int = 0) {
  require(name.nonEmpty, "name must not be null or empty")
}
