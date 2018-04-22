package personalfinance
package businesslogic
package transaction

/**
  * This is the pattern class which will be a part of Category
  */
case class Patterns(list: List[Pattern])

case class Pattern(value: String, id: Int = 0)
