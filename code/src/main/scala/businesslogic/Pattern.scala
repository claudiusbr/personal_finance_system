package businesslogic

import scala.util.matching.Regex

object Pattern {
  def apply(pattern: Regex, categories: List[Category]): Pattern =
    if (categories.isEmpty)
      throw new IllegalArgumentException("All patterns must have at least one Category")
    else
      new Pattern(pattern,categories)
}
case class Pattern private (pattern: Regex, categories: List[Category])
