import java.util.stream.LongStream;

class CollatzCalculator {

	int computeStepCount(int start) {
		if (start <= 0) {
			throw new IllegalArgumentException("Only positive integers are allowed");
		}

		return (int) LongStream.iterate(start, this::isNotOne, this::nextCollatzValue)
				             .count();
	}

	private long nextCollatzValue(long n) {
		return (n % 2 == 0)
            ? n / 2
            : 3 * n + 1;
	}

	private boolean isNotOne(long n) {
		return n != 1;
	}
}
