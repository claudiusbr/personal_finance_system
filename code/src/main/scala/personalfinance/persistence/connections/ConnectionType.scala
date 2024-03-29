package personalfinance
package persistence
package connections

/**
  * This trait needs to be implemented by classes which will hold the hardcoded
  * references to the query dialect they represent
  */
private[persistence] sealed trait ConnectionType {
  protected val category = s"$dbName.category"
  protected val pattern = s"$dbName.pattern"
  protected val entry = s"$dbName.entry"
  protected val entry_description = s"$dbName.entry_description"

  def dbName: String

  def queryForAllCategoriesAndPatterns: String

  def queryForACategory(name: String): String

  def queryForCategoryPatterns(id: Int): String

  def createCategoryOnly(name: String): String

  def createPatternOnly(categoryId: Int, patternValue: String): String

  def createEntryDescriptionPS: String

  def getEntryDescription(description: String): String

  def getSummaryPS(): String

  def createEntryPS: String
}

/**
  * this is an implementation of MySql's dialect
  */
private[persistence] final case class MySql(_dbName: String)
  extends ConnectionType {

  override def dbName: String = _dbName

  override def queryForAllCategoriesAndPatterns: String =
    s"""select *
      |from $category cat
      |   left join $pattern pat
      |     on cat.idcategory = pat.category_id;""".stripMargin

  override def queryForACategory(name: String): String =
    s"""select idcategory,name
       |from $category cat
       |where cat.name = '$name';
     """.stripMargin

  override def queryForCategoryPatterns(catId: Int): String =
    s"select idpattern,value,category_id from $pattern where category_id = $catId"

  override def createCategoryOnly(catName: String): String =
    s"insert into $category (`name`) values ('$catName')"

  override def createPatternOnly(categoryId: Int, patternValue: String): String =
    s"insert into $pattern (`value`,`category_id`) " +
      s"values ('$patternValue', '$categoryId');"

  override def createEntryDescriptionPS: String =
    s"insert into $entry_description (`date_created`, `date_recorded`, `value`) values (?,?,?)"

  override def getEntryDescription(description: String): String =
    s"select id_entry_description,value from $entry_description " +
      s"where value = '$description'" +
      s"and date_created = (?)" +
      s"and date_recorded = (?)"

  override def createEntryPS: String =
    s"insert into $entry (`amount`, `category_id`, `description_id`, `currency_id`)" +
      "values (?, ?, ?, ?)"

  override def getSummaryPS(): String =
    "select cat.name as Category,sum(amount)*-1 as Total " +
    "from category cat left join entry on entry.category_id = cat.idcategory " +
    "join entry_description on entry.description_id = entry_description.id_entry_description " +
    "where date_created >= (?) and date_created <= (?) " +
    "group by cat.name " +
    "order by sum(amount)"
}

/**
  * This is a placeholder for a possible future
  * implementation of a H2 database
  */
private[persistence] final case class H2(_dbName: String)
  extends ConnectionType {

  override def dbName: String = _dbName

  override def queryForAllCategoriesAndPatterns: String = ???

  override def queryForACategory(name: String): String = ???

  override def queryForCategoryPatterns(id: Int): String = ???

  override def createCategoryOnly(name: String): String = ???

  override def createPatternOnly(categoryId: Int, patternValue: String): String = ???

  override def createEntryDescriptionPS: String = ???

  override def getEntryDescription(description: String): String = ???

  override def createEntryPS: String = ???

  override def getSummaryPS(): String = ???
}
