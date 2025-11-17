object ProteinTranslation {
  val stop = "STOP"
  val correspondingProteinByCodon: Map[String, String] = Map(
    "AUG" -> "Methionine",
    "UAA" -> stop,
    "UAC" -> "Tyrosine",
    "UAG" -> stop,
    "UAU" -> "Tyrosine",
    "UCA" -> "Serine",
    "UCC" -> "Serine",
    "UCG" -> "Serine",
    "UCU" -> "Serine",
    "UGA" -> stop,
    "UGC" -> "Cysteine",
    "UGG" -> "Tryptophan",
    "UGU" -> "Cysteine",
    "UUA" -> "Leucine",
    "UUC" -> "Phenylalanine",
    "UUG" -> "Leucine",
    "UUU" -> "Phenylalanine"
  )

  def proteins(rna: String): Seq[String] =
    splitWords(rna)
      .takeWhile(_ != stop)

  def splitWords(rna: String): Seq[String] =
    rna
      .split("")
      .grouped(3)
      .map(toProtein)
      .toSeq

  def toProtein(letters: Array[String]): String =
    correspondingProteinByCodon(letters.mkString(""))
}