package personalfinance.input

class CSVInput (file: String) extends Input {
  val lines: Iterable[String] = {
    io.Source.fromFile(this.file).getLines().toIterable
  }
}

