object EliudsEggs {

	fun eggCount(number: Int): Int {
		return countBits(number)
	}

	private fun countBits(number: Int): Int {
		return when (number) {
			0    ->
				0

			else ->
				(number and 1) +
					countBits(number ushr 1)
		}
	}
}