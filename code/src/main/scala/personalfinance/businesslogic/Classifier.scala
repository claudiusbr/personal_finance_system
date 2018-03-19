package personalfinance
package businesslogic

import personalfinance.businesslogic.transaction._

/**
  * this class is responsible for matching entries against
  * existing categories
  */
class Classifier {

  /**
    * this is the method responsible for matching entries to categories
    * @param categories list of a Map of categories, where each Map
    *                   instance should be made up of all patterns
    *                   which map to the same instance of category
    * @param entries a list of uncategorised entries
    * @return a Tuple2 of the categorised transaction units and
    *         still unmatched entries
    */
  def classify(categories: List[Map[String,Category]], entries: List[Entry]):
    (List[TransactionUnit], List[Entry]) =
    classifyByDescription(categories,entries)

  private def classifyByDescription(categories: List[Map[String,Category]],
    entries: List[Entry]): (List[TransactionUnit], List[Entry]) = {
    entries.map(
      entry => (entry,categories.find(_.contains(entry.description))) match {
        case (e: Entry, Some(map)) =>
          TransactionUnit(map(e.description),List(e))
        case (e: Entry, None) => e
      }
    ).partition(_.isInstanceOf[TransactionUnit])
      .asInstanceOf[(List[TransactionUnit],List[Entry])]
  }
}