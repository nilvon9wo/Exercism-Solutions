object Diamond {
    def rows(letter: Char): List[String] =
        val size = (letter - 'A') + 1
        val topHalf = (0 until size)
            .toList.map(buildRow(size))
        val bottomHalf = topHalf.reverse.tail
        topHalf ++ bottomHalf
    }

    def buildRow(size: Int)(i: Int): String =
        val currentChar = ('A' + i).toChar
        val outerSpaces = size - i - 1
        val innerSpaces = i * 2 - 1

        if (i == 0) {
            " " * outerSpaces + "A" + " " * outerSpaces
        }
        else {
            " " * outerSpaces + currentChar
                + " " * innerSpaces + currentChar
                + " " * outerSpaces
        }
