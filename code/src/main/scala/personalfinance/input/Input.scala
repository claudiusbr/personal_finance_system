package personalfinance
package input

import java.io.FileNotFoundException

class Input  {
  def lines(file: String): Option[Seq[String]] = {
    try {
      Some(io.Source.fromFile(file).getLines().toSeq)
    } catch {
      case _: FileNotFoundException => None
      case k: Throwable => throw k
    }
  }
}
