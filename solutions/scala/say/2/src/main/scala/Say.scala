import scala.annotation.tailrec

object Say:
    private val ones: Map[Int, String] = Map(
        0 -> "zero",
        1 -> "one",
        2 -> "two",
        3 -> "three",
        4 -> "four",
        5 -> "five",
        6 -> "six",
        7 -> "seven",
        8 -> "eight",
        9 -> "nine",
        10 -> "ten",
        11 -> "eleven",
        12 -> "twelve",
        13 -> "thirteen",
        14 -> "fourteen",
        15 -> "fifteen",
        16 -> "sixteen",
        17 -> "seventeen",
        18 -> "eighteen",
        19 -> "nineteen"
    )

    private val tens: Map[Int, String] = Map(
        20 -> "twenty",
        30 -> "thirty",
        40 -> "forty",
        50 -> "fifty",
        60 -> "sixty",
        70 -> "seventy",
        80 -> "eighty",
        90 -> "ninety"
    )

    private val scales: Vector[String] = Vector(
        "",
        "thousand",
        "million",
        "billion",
        "trillion"
    )

    private def scaleWord(scaleIndex: Int): String =
        scales.lift(scaleIndex)
              .getOrElse("")

    def inEnglish(number: Long): Option[String] =
        if number < 0 || number > 999999999999L
        then None
        else Some(convertNumberToWords(number))

    private def convertNumberToWords(number: Long): String =
        if number == 0
        then "zero"
        else buildFullNumberWords(number)

    private def buildFullNumberWords(number: Long): String =
        val chunks = splitIntoThousands(number)
        val totalChunks = chunks.length
        chunks.zipWithIndex
              .map { case (chunk, index) =>
                  formatChunkWithScale(totalChunks, chunk, index)
              }
              .filter(_.nonEmpty)
              .mkString(" ")

    private def formatChunkWithScale(
                                        totalChunks: Int,
                                        chunk: Int,
                                        index: Int
                                    ): String =
        val scaleIndex = totalChunks - index - 1
        val chunkWords = convertBelowThousand(chunk)

        if chunkWords.isEmpty
        then ""
        else attachScale(scaleIndex, chunkWords)

    private def attachScale(scaleIndex: Int, words: String): String =
        val scale = scaleWord(scaleIndex)
        if scale.isEmpty
        then words
        else s"$words $scale"

    private def splitIntoThousands(number: Long): List[Int] =
        @tailrec
        def loop(x: Long, accumulator: List[Int]): List[Int] =
            if x == 0
            then accumulator
            else loop(x / 1000, (x % 1000).toInt :: accumulator)

        loop(number, Nil)

    private def convertBelowThousand(number: Int): String =
        number match
            case 0 => ""

            case x if x < 20 =>
                ones(x)

            case x if x < 100 =>
                val tensPart = (x / 10) * 10
                val onesPart = x % 10

                if onesPart == 0
                then tens(tensPart)
                else s"${tens(tensPart)}-${ones(onesPart)}"

            case x =>
                val hundreds = x / 100
                val remainder = x % 100

                if remainder == 0
                then s"${ones(hundreds)} hundred"
                else s"${ones(hundreds)} hundred ${convertBelowThousand(remainder)}"
                
                