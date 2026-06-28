object PersonSetEnumerator {

    def enumerate(allCandidates: Array[Person]): Iterator[List[Person]] =
        enumerate(allCandidates, List.empty)

    private def enumerate(
                          allCandidates: Array[Person],
                          partialCandidateSet: List[Person]
                      ): Iterator[List[Person]] =
        val candidateSet: List[Person] = partialCandidateSet

        if candidateSet.size == 5
        then Iterator.single(candidateSet)
        else extendCandidates(candidateSet, allCandidates)

    private def extendCandidates(
                                existingCandidates: List[Person],
                                allCandidates: Array[Person]
                            ): Iterator[List[Person]] =
        allCandidates.iterator
                     .filter(candidate => isConsistent(existingCandidates, candidate))
                     .flatMap { candidate =>
                         enumerate(
                             allCandidates,
                             existingCandidates.appended(candidate)
                         )
                     }
                     .distinct

    private def isConsistent(
                                    existingPeople: List[Person],
                                    candidate: Person
                                ): Boolean =
        val existingArray = existingPeople.toArray
        !existingArray.contains(candidate) &&
            existingArray.forall(existing => existing.noConflict(candidate))
}