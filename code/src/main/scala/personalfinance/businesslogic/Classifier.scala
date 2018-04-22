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
  def classify(categories: List[Category], entries: List[Entry]):
    (List[TransactionUnit], List[Entry]) =
    classifyByDescription(categories,entries)

  private def classifyByDescription(categories: List[Category],
    entries: List[Entry]): (List[TransactionUnit], List[Entry]) = {
    entries.map({
      entry => (entry,categories.find({
          cat => cat.patterns.list.foldLeft(false)({
            (test,pat) => entry.description.contains(pat.value) || test})
      })) match {
        case (e: Entry, Some(cat)) =>
          TransactionUnit(cat,List(e))
        case (e: Entry, None) => e
      }
    }).partition(_.isInstanceOf[TransactionUnit])
      .asInstanceOf[(List[TransactionUnit],List[Entry])]
  }
}
