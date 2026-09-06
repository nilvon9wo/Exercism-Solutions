object Bob {
	fun hey(input: String): String {
		val trimmedInput = input.trim()
		if (trimmedInput.isEmpty()) {
			return "Fine. Be that way!"
		}

		val isQuestion = trimmedInput.endsWith("?")
		val letters = trimmedInput.filter { it.isLetter() }
		val isYelling = letters.isNotEmpty()
		                && letters.all { it.isUpperCase() }

		return when {
			isQuestion && isYelling
				-> "Calm down, I know what I'm doing!"

			isQuestion
				-> "Sure."

			isYelling
				-> "Whoa, chill out!"

			else
				-> "Whatever."
		}
	}
}