package personalfinance
package input

import java.io.FileNotFoundException

class Input  {
  def lines(file: String): Option[Iterable[String]] = {
    try {
      Some(io.Source.fromFile(file).getLines().toIterable)
    } catch {
      case _: FileNotFoundException => None
      case k: Throwable => throw k
    }
  }
}

