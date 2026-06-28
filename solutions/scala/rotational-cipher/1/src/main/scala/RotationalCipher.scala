object RotationalCipher:
    def rotate(text: String, shift: Int): String =
        val normalizedShift = shift % 26
        val rotateCharacter  = applyRotation(normalizedShift)
        text.map(rotateCharacter )

    private def applyRotation(shift: Int)(character: Char): Char =
        if character >= 'a' && character <= 'z'
        then shiftToLowercase(shift, character)
        else if character >= 'A' && character <= 'Z'
             then shiftToUppercase(shift, character)
             else character

    private def shiftToUppercase(shift: Int, character: Char): Char =
        rotateWithinAlphabet(shift, character, 'A')

    private def shiftToLowercase(shift: Int, character: Char): Char =
        rotateWithinAlphabet(shift, character, 'a')

    private def rotateWithinAlphabet(shift: Int, character: Char, alphabetStart: Char): Char =
        val base = alphabetStart.toInt
        val offset = character.toInt - base + shift
        (offset % 26 + base).toChar