import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

final class PersonSetFinder {

    static Stream<Set<Person>> findDistinctPersonSets(Person[] allCandidates) {
        return findDistinctPersonSets(allCandidates, new HashSet<>());
    }

    private static Stream<Set<Person>> findDistinctPersonSets(
            Person[] allCandidates,
            Set<Person> partialSet
    ) {
        if (partialSet.size() == 5) {
            return Stream.of(Set.copyOf(partialSet));
        }

        return findCompatibleCandidates(partialSet, allCandidates);
    }

    private static Stream<Set<Person>> findCompatibleCandidates(
            Set<Person> existingCandidates,
            Person[] allCandidates
    ) {
        return Arrays.stream(allCandidates)
                     .filter(candidate -> isCandidateValid(existingCandidates, candidate))
                     .flatMap(candidate -> {
                         Set<Person> nextSet = new HashSet<>(existingCandidates);
                         nextSet.add(candidate);

                         return findDistinctPersonSets(allCandidates, nextSet)
                                 .distinct();
                     })
                     .distinct();
    }

    private static boolean isCandidateValid(
            Set<Person> existingPeople,
            Person candidate
    ) {
        return !existingPeople.contains(candidate)
               && existingPeople.stream()
                                .allMatch(existing -> existing.noConflict(candidate));
    }
}