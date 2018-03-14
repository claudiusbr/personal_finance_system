package businesslogic
package transaction

import scala.util.matching.Regex

/**
  * Creates Categories, which are the implementation of Fowler's Account pattern.
 *
  * @param name the class name
  * @param entries a list of Entry types
  */
case class Category private[transaction] (name: String, entries: List[Entry], patterns: Patterns) {
  def this(name: String) = this(name,List[Entry](),new Patterns(List[Regex]()))
  def this(name: String, patterns: Patterns) = this(name,List[Entry](),patterns)
}
