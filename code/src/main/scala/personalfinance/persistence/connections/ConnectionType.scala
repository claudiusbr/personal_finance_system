package personalfinance
package persistence
package connections

private[persistence] sealed trait ConnectionType {
  def queryForAllCategories: String
}

private[persistence] final case class MySql() extends ConnectionType {
  def queryForAllCategories: String = "select * from categories"
}

private[persistence] final case class H2() extends ConnectionType {
  def queryForAllCategories: String = "select * from categories"
}
