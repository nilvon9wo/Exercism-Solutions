object Anagram {
  def findAnagrams(base: String, candidates: List[String]): List[String] =
    candidates.filterNot(base.toUpperCase == _.toUpperCase())
    .filterNot(base.toUpperCase.toList.sorted != _.toUpperCase.toList.sorted)
}