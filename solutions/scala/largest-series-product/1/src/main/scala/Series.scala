object Series {
  def largestProduct(size: Int, series: String): Option[Int] =
    if (size == 0) {
      Some(1)
    }
    else if (this.isInvalidInput(size, series)) {
      None
    }
    else {
      Some(this.findMaxProduct(size, series))
    }

  private def isInvalidInput(size: Int, series: String): Boolean =
    size < 0 ||
      size > series.length ||
      series.isBlank ||
      !series.forall(Character.isDigit)

  private def findMaxProduct(size: Int, series: String) = {
    this.findProducts(size, series)
      .maxBy(_._2)
      ._2
  }

  private def findProducts(size: Int, series: String): Map[String, Int] =
    (0 to (series.length - size))
      .map(extractSubseries(size, series))
      .map(pairToProduct)
      .toMap

  private def extractSubseries(size: Int, series: String)(start: Int): String =
    series.slice(start, start + size)

  private def pairToProduct(substring: String): (String, Int) = {
    val product = substring.split("")
      .map(_.toInt)
      .product
    (substring, product)
  }
}