object Etl {
  def transform(lettersByPoints: Map[Int, Seq[String]]): Map[String, Int] =
    lettersByPoints
      .flatMap {
                 case (points, letters) =>
                   letters.map(letter => letter.toLowerCase -> points)
               }
}
