class DifferenceOfSquaresCalculator {
	private static final int ARITHMETIC_SERIES_DIVISOR = 2;
	private static final int SUM_OF_SQUARES_DIVISOR = 6;

	int computeSquareOfSumTo(int n) {
		int sum = n * (n + 1) / ARITHMETIC_SERIES_DIVISOR;
		return sum * sum;
	}

	int computeSumOfSquaresTo(int n) {
		return n * (n + 1) * (2 * n + 1)
				       / SUM_OF_SQUARES_DIVISOR;
	}

	int computeDifferenceOfSquares(int n) {
		return this.computeSquareOfSumTo(n)
				       - this.computeSumOfSquaresTo(n);
	}
}
