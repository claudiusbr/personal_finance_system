package personalfinance
package persistence
package connections

private[persistence] sealed trait ConnectionType {
  protected val dbName = "personal_finance_system"

  protected val category = s"$dbName.category"
  protected val pattern = s"$dbName.pattern"
  protected val entry = s"$dbName.entry"
  protected val entry_description = s"$dbName.entry_description"

  def queryForAllCategories: String

  def queryForACategory(name: String): String
}

private[persistence] final case class MySql() extends ConnectionType {
  override def queryForAllCategories: String =
    s"""select *
      |from $category cat
      |   left join $pattern pat
      |     on cat.idcategory = pat.category_id;""".stripMargin

  override def queryForACategory(name: String): String =
    s"""select idcategory,name
       |from $category cat
       |where cat.name = '$name';
     """.stripMargin
}

/**
  * This is a placeholder for a possible future
  * implementation of H2 databases
  */
private[persistence] final case class H2() extends ConnectionType {
  override def queryForAllCategories: String = ???

  override def queryForACategory(name: String): String = ???
}
