object RnaTranscription {
  private val adenine                      = 'A'
  private val cytosine                     = 'C'
  private val guanine                      = 'G'
  private val thymine                      = 'T'
  private val uracil                       = 'U'

  private val rnaComplementByDnaNucleotide = Map(
    guanine -> cytosine,
    cytosine -> guanine,
    thymine -> adenine,
    adenine -> uracil
    )

  def toRna(dna: String): Option[String] = {
    val rnaComplements = dna.toList.map(toRna)
    if (rnaComplements.forall(rnaComplement => rnaComplement.isDefined)) {
      Some(rnaComplements.flatten.mkString)
    }
    else {
      None
    }
  }

  def toRna(nucleotide: Char): Option[Char] = rnaComplementByDnaNucleotide.get(nucleotide)
}
