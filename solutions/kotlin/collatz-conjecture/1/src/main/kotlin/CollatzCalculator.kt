object CollatzCalculator {
	fun computeStepCount(start: Int): Int {
		require(start > 0)
		return countSteps(start, 0)
	}

	private tailrec fun countSteps(current: Int, steps: Int): Int {
		return if (current == 1) {
			steps
		}
		else if (current % 2 == 0) {
			countSteps(current / 2, steps + 1)
		}
		else {
			countSteps(current * 3 + 1, steps + 1)
		}
	}
}