import java.util.Objects;
import java.util.stream.IntStream;

public class Hamming {
	private final int length;
	private final String leftStrand;
	private final String rightStrand;

	public Hamming(String leftStrand, String rightStrand) {
		this.leftStrand = Objects.requireNonNull(leftStrand, "leftStrand must not be null");
		this.rightStrand = Objects.requireNonNull(rightStrand, "rightStrand must not be null");

		this.length = leftStrand.length();
		if (this.length != rightStrand.length()) {
			throw new IllegalArgumentException("strands must be of equal length");
		}

	}

	public int getHammingDistance() {
		return IntStream.range(0, length)
				       .map(this::doesLetterMatch)
				       .sum();
	}

	private int doesLetterMatch(int i) {
		return leftStrand.charAt(i) == rightStrand.charAt(i)
				            ? 0
				            : 1;
	}
}
