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
                                         patterns: Patterns = Patterns(List[Pattern]()),
                                         id: Option[Int] = None) {
  require(name.nonEmpty, "name must not be null or empty")
}
