import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

record NucleotideCounter(String sequence) {

    private static final Set<Character> VALID_NUCLEOTIDES = Set.of('A', 'C', 'G', 'T');
    NucleotideCounter {
        this.ensureAllNucleotidesValid(sequence);
    }

    private void ensureAllNucleotidesValid(String sequence) {
        boolean containsInvalidNucleotide =
                sequence.chars()
                        .mapToObj(c -> (char) c)
                        .anyMatch(nucleotide -> !VALID_NUCLEOTIDES.contains(nucleotide));

        if (containsInvalidNucleotide) {
            throw new IllegalArgumentException("Invalid nucleotide in sequence");
        }
    }

    Map<Character, Integer> nucleotideCounts() {
        Map<Character, Integer> initialCountByNucleotides = this.initializeZeroCounts();
        Map<Character, Long> observedCountsByNucleotides = this.countNucleotidesInSequence();
        return this.mergeZeroAndObservedCounts(initialCountByNucleotides, observedCountsByNucleotides);
    }

    private Map<Character, Integer> initializeZeroCounts() {
        return VALID_NUCLEOTIDES.stream()
                       .collect(this.zeroCountInitializer());
    }

    private Collector<Character, ?, Map<Character, Integer>> zeroCountInitializer() {
        return Collectors.toMap(nucleotide -> nucleotide, nucleotide -> 0);
    }

    private Map<Character, Long> countNucleotidesInSequence() {
        return sequence.chars()
                       .mapToObj(nucleotide -> (char) nucleotide)
                       .collect(this.countOccurrencesByNucleotide());
    }

    private Collector<Character, ?, Map<Character, Long>> countOccurrencesByNucleotide() {
        return Collectors.groupingBy(
                nucleotide -> nucleotide,
                Collectors.counting()
        );
    }

    private Map<Character, Integer> mergeZeroAndObservedCounts(
            Map<Character, Integer> zeroCounts,
            Map<Character, Long> observedCounts
    ) {
        return zeroCounts.entrySet()
                       .stream()
                       .collect(this.toMergedCountMap(observedCounts));
    }

    private Collector<Map.Entry<Character, Integer>, ?, Map<Character, Integer>> toMergedCountMap(
            Map<Character, Long> observedCounts
    ) {
        return Collectors.toMap(
                Map.Entry::getKey,
                entry -> this.resolveObservedCount(observedCounts, entry)
        );
    }

    private int resolveObservedCount(
            Map<Character, Long> observedCounts,
            Map.Entry<Character, Integer> entry
    ) {
        Character nucleotide = entry.getKey();
        return this.observedCountFor(nucleotide, observedCounts);
    }

    private int observedCountFor(Character nucleotide, Map<Character, Long> observedCounts) {
        return observedCounts.getOrDefault(nucleotide, 0L)
                       .intValue();
    }
}
