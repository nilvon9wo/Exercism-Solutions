object Darts:

    def score(x: Double, y: Double): Int =
        val distanceSquared = (x * x) + (y * y)
        distanceSquared match
            case _ if distanceSquared <= 1.0 => 10
            case _ if distanceSquared <= 25.0 => 5
            case _ if distanceSquared <= 100.0 => 1
            case _ => 0
