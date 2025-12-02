import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ProteinTranslator {
	private static final int CODON_LENGTH = 3;
	private static final String STOP = "STOP";

	private static final MultiKeyMap<String> CODON_TO_PROTEIN =
			MultiKeyMap.fromMapOfSets(Map.ofEntries(
					Map.entry(Set.of("AUG"), "Methionine"),
					Map.entry(Set.of("UUU", "UUC"), "Phenylalanine"),
					Map.entry(Set.of("UUA", "UUG"), "Leucine"),
					Map.entry(Set.of("UCU", "UCC", "UCA", "UCG"), "Serine"),
					Map.entry(Set.of("UAU", "UAC"), "Tyrosine"),
					Map.entry(Set.of("UGU", "UGC"), "Cysteine"),
					Map.entry(Set.of("UGG"), "Tryptophan"),
					Map.entry(Set.of("UAA", "UAG", "UGA"), STOP)
			));

	List<String> translate(String rnaSequence) {
		int totalCodons = rnaSequence.length() / CODON_LENGTH;
		List<String> proteins = this.translate(rnaSequence, totalCodons);
		if (this.hasInvalidTrailingCodons(rnaSequence, proteins, totalCodons)) {
			throw new IllegalArgumentException("Invalid codon");
		}

		return proteins;
	}

	private List<String> translate(String rnaSequence, int totalCodons) {
		return IntStream.range(0, totalCodons)
				       .mapToObj(i -> this.codonAt(rnaSequence, i))
				       .map(this::translateCodon)
				       .takeWhile(protein -> !STOP.equals(protein))
				       .collect(Collectors.toList());
	}

	private boolean hasInvalidTrailingCodons(String rnaSequence, List<String> translatedProteins, int totalCodons) {
		int translatedCodonsCount = translatedProteins.size();
		boolean hasMoreCodonsRemaining = translatedCodonsCount < totalCodons;
		boolean translationStoppedEarly = hasMoreCodonsRemaining
				                                  && this.isStopCodonAtIndex(rnaSequence, translatedCodonsCount);
		int leftoverNucleotides = rnaSequence.length() % CODON_LENGTH;
		boolean hasIncompleteCodon = leftoverNucleotides != 0;
		return !translationStoppedEarly && hasIncompleteCodon;
	}

	private boolean isStopCodonAtIndex(String rnaSequence, int codonIndex) {
		String codon = this.codonAt(rnaSequence, codonIndex);
		String protein = this.translateCodon(codon);
		return STOP.equals(protein);
	}


	private String codonAt(String rnaSequence, int codonIndex) {
		int beginIndex = codonIndex * CODON_LENGTH;
		return rnaSequence.substring(beginIndex, beginIndex + CODON_LENGTH);
	}

	private String translateCodon(String codon) {
		String protein = CODON_TO_PROTEIN.get(codon);
		if (protein == null) {
			throw new IllegalArgumentException("Invalid codon");
		}

		return protein;
	}
}
