import java.util.regex.Pattern

object PhoneNumber {
  private val areaCodeIndex     = 0
  private val exchangeCodeIndex = 3
  private val restrictedIndices = Set(areaCodeIndex, exchangeCodeIndex)
  private val zero              = '0'
  private val one               = '1'
  private val restrictedValues = Set(zero, one)
  private val requiredPhoneNumberLength = 10

  private val nonDigitPattern: Pattern = Pattern.compile("[^0-9]")

  def clean(phoneNumber: String): Option[String] = {
    val cleanedNumber: String = cleanNumber(phoneNumber)
    if (isInvalid(cleanedNumber)) {
      None
    }
    else {
      Some(cleanedNumber)
    }
  }

  private def cleanNumber(phoneNumber: String): String = {
    val cleanNumber = nonDigitPattern.matcher(phoneNumber)
                                     .replaceAll("")
                                     .toList match {
      case `one` :: tail       => tail
      case nonOneStartingDigit => nonOneStartingDigit
    }
    cleanNumber.mkString
  }

  private def isInvalid(cleanedNumber: String) = {
    hasWrongLength(cleanedNumber) || hasIllegalCharacter(cleanedNumber)
  }

  private def hasWrongLength(cleanedNumber: String) = {
    cleanedNumber.length != requiredPhoneNumberLength
  }

  private def hasIllegalCharacter(cleanedNumber: String): Boolean =
    restrictedIndices.exists(index => {
      val indexedValue = cleanedNumber(index)
      restrictedValues.contains(indexedValue)
    })
}
