object KillerSudokuHelper:
    def combinations(sum: Int, size: Int, exclude: List[Int]): List[List[Int]] =
        findCombinations(
            remainingSum = sum,
            remainingSize = size,
            availableDigits = (1 to 9).filterNot(exclude.contains).toList
        )

    private def findCombinations(
                                    remainingSum: Int,
                                    remainingSize: Int,
                                    availableDigits: List[Int]
                                ): List[List[Int]] = {

        if (remainingSize == 0)
            return if remainingSum == 0
                   then List(Nil)
                   else Nil

        availableDigits match {
            case Nil =>
                Nil

            case digit :: remainingDigits =>
                val withDigit =
                    findCombinations(remainingSum - digit, remainingSize - 1, remainingDigits)
                        .map(digit :: _)

                val withoutDigit =
                    findCombinations(remainingSum, remainingSize, remainingDigits)
                withDigit ++ withoutDigit
        }
    }