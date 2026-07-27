object Darts {

	fun score(x: Number, y: Number): Int {
		val xCoordinate = x.toDouble()
		val yCoordinate = y.toDouble()

		val distanceSquared = xCoordinate * xCoordinate + yCoordinate * yCoordinate
		return when {
			distanceSquared <= 1
				-> 10

			distanceSquared <= 25
				-> 5

			distanceSquared <= 100
				-> 1

			else
				-> 0
		}
	}
}