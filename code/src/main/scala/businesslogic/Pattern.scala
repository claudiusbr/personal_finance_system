package businesslogic

import scala.util.matching.Regex

case class Pattern(pattern: Regex, categories: List[Category])
