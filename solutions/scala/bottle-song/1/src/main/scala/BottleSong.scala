object BottleSong:

    private val numbers = Array(
        "no", "one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine", "ten"
    )

    def recite(start: Int, count: Int): String =
        (0 until count)
            .map(i => verse(start - i))
            .mkString("\n\n") + "\n"

    private def verse(n: Int): String =
        val current = capitalize(bottlePhrase(n))
        val next = bottlePhrase(n - 1)
        s"""$current hanging on the wall,
           |$current hanging on the wall,
           |And if one green bottle should accidentally fall,
           |There'll be $next hanging on the wall.""".stripMargin

    private def bottlePhrase(n: Int): String =
        val word = numbers(n)
        val bottle = if n == 1
                     then "bottle"
                     else "bottles"
        s"$word green $bottle"

    private def capitalize(word: String): String =
        if word.isEmpty
        then word
        else word.head.toUpper.toString + word.tail
