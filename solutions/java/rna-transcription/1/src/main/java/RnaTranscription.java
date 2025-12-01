import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

class RnaTranscription {
	private static final Map<Character, Character> RNA_COMPLEMENT_BY_DNA = Map.of(
				'G', 'C',
				'C', 'G',
				'T', 'A',
				'A', 'U'
	);

    String transcribe(String dnaStrand) {
	    return dnaStrand.chars()
			           .mapToObj(this.getRnaCharacter())
			           .map(String::valueOf)
			           .collect(Collectors.joining());
    }

	private IntFunction<Character> getRnaCharacter() {
		return i -> getRna((char) i);
	}

	private char getRna(char dna) {
		Character rna = RNA_COMPLEMENT_BY_DNA.get(dna);
		if (rna == null) {
			throw new IllegalArgumentException("Invalid nucleotide: " + dna);
		}

		return rna;
	}

}
