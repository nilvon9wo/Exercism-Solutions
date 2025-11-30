import java.math.BigInteger;
import java.util.stream.IntStream;

class Grains {

	private static final int MIN_SQUARE = 1;
	private static final int MAX_SQUARE = 64;
	private static final BigInteger INITIAL_SUM = BigInteger.ZERO;

	BigInteger grainsOnSquare(final int square) {
		if (square < MIN_SQUARE || square > MAX_SQUARE) {
			throw new IllegalArgumentException(
					"square must be between " + MIN_SQUARE + " and " + MAX_SQUARE
			);
		}

		// Equivalent to (2^n) for arbitrary large n.
		return BigInteger.ONE.shiftLeft(square - 1);
	}

	BigInteger grainsOnBoard() {
		return IntStream.rangeClosed(MIN_SQUARE, MAX_SQUARE)
				       .mapToObj(this::grainsOnSquare)
				       .reduce(INITIAL_SUM, BigInteger::add);
	}
}
