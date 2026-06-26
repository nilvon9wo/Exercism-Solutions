object CryptoSquare:
    def ciphertext(plaintext: String): String =
        val normalized = normalize(plaintext)
        if normalized.isEmpty
        then ""
        else encode(normalized)

    private def encode(normalized: String) =
        val rectangle = buildRectangle(normalized)
        readColumns(rectangle)
            .mkString(" ")

    private def normalize(text: String): String =
        text.filter(_.isLetterOrDigit)
            .map(_.toLower)

    private def buildRectangle(text: String): Vector[String] =
        val width = rectangleWidth(text.length)
        val padding = paddedLength(text.length, width)
        text.padTo(width * padding, ' ')
            .grouped(width)
            .toVector

    private def rectangleWidth(length: Int): Int =
        val squareRoot = math.sqrt(length.toDouble)
        math.ceil(squareRoot).toInt

    private def paddedLength(length: Int, width: Int): Int =
        val requiredHeight = length.toDouble / width
        math.ceil(requiredHeight ).toInt

    private def readColumns(rows: Vector[String]): Vector[String] =
        (0 until rows.head.length)
            .map(columnIndex => readColumn(rows, columnIndex))
            .toVector

    private def readColumn(rows: Vector[String], columnIndex: Int): String =
        rows.map(row => row(columnIndex))
            .mkString