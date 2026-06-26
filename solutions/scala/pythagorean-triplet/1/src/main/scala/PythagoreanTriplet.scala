object PythagoreanTriplet:
    private type Triplet = (Int, Int, Int)

    def isPythagorean(triplet: Triplet): Boolean =
        val (a, b, c) = triplet
        a * a + b * b == c * c

    def pythagoreanTriplets(
                               minimumFactor: Int,
                               maximumFactor: Int
                           ): Seq[Triplet] =
        for
            a <- minimumFactor until maximumFactor - 1
            b <- (a + 1) until maximumFactor
            c <- (b + 1) to maximumFactor
            triplet = (a, b, c)
            if isPythagorean(triplet)
        yield triplet

    def pythagoreanTripletsSum(sum: Int): Seq[Triplet] =
        for
            a <- 1 until sum / 3
            b <- (a + 1) until sum / 2
            c = sum - a - b
            triplet = (a, b, c)
            if b < c
            if isPythagorean(triplet)
        yield triplet