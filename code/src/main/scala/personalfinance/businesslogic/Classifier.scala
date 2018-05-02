package personalfinance
package businesslogic

import personalfinance.businesslogic.transaction._

trait Classifier {
  def classify(categories: Seq[Category], entries: Seq[Entry]):(Seq[TransactionUnit], Seq[Entry])
}

/**
  * this class is responsible for matching entries against
  * existing categories
  */
class StringClassifier extends Classifier {

  /**
    * this is the method responsible for matching entries to categories
    * @param categories list of a Map of categories, where each Map
    *                   instance should be made up of all patterns
    *                   which map to the same instance of category
    * @param entries a list of uncategorised entries
    * @return a Tuple2 of the categorised transaction units and
    *         still unmatched entries
    */
  def classify(categories: Seq[Category],
               entries: Seq[Entry]): (Seq[TransactionUnit], Seq[Entry]) =
    classifyByDescription(categories,entries)


  private def classifyByDescription(categories: Seq[Category],
    entries: Seq[Entry]): (Seq[TransactionUnit], Seq[Entry]) = {

    // extract all the patterns from categories and sort
    // them by length, from longest to shortest
    val patternIndex: Seq[String] = categories
      .flatMap(c=>{c.patterns.list.map( _.value )})
      .sortBy(_.length)
      .reverse

    // match the full pattern against the prefix of every
    // entry description
    entries.map({
      entry =>
        (entry,patternIndex.find(
          entry.description.startsWith)) match {

        // if it finds them, return a transaction unit for
        // that category
        case (e: Entry, Some(pat)) =>
          TransactionUnit(categories.find(cat => {
            cat.patterns.list.foldLeft(false)({
              (test,catPat) =>
                catPat
                  .value
                  .toLowerCase() == pat.toLowerCase() || test
            })
          }).get,Seq(e))

        // if not, return the entry as uncategorised
        case (e: Entry, None) => e
      }
    }).partition(_.isInstanceOf[TransactionUnit])
      .asInstanceOf[(Seq[TransactionUnit],Seq[Entry])]
  }
}
