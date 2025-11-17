import DNA.validNucleotides

case class DNA(nucleotides: String) {
  val nucleotideCounts: Either[String, Map[Char, Int]] =
    if (nucleotides.forall(validNucleotides.contains)) {
      Right(validNucleotides.map(nucleotideCountPair).toMap)
    } else {
      Left("Invalid DNA sequence")
    }

  private def nucleotideCountPair(nucleotides: Char) =
    nucleotides -> mapNucleotides.getOrElse(nucleotides, 0)

  private lazy val mapNucleotides =
    nucleotides.groupBy(identity)
               .view
               .mapValues(_.length)
               .toMap
}

object DNA {
  val validNucleotides: Set[Char] = Set('A', 'C', 'G', 'T')
}
