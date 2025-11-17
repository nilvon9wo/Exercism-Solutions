import MapUtilities.RichMap
import SetUtilities.RichHashSet

import scala.annotation.tailrec

object Alphametics {
  private val SingleDigitNumbers   : List[Int]                                     = (0 to 9)
    .toList
  private val calculateFactorWeight: List[Int] => PartialFunction[(Int, Int), Int] = permutation => {
    case (key, count) =>
      count * permutation(key)
  }

  def solve(equation: String): Option[Map[Char, Int]] = {
    val activeLetters     : Iterable[Char]  = extractUnknowns(equation)
    val activeLettersArray: Array[Char]     = activeLetters.toArray
    val activeLetterCount : Int             = activeLettersArray.length
    val mappedCharacters                    = IterableUtilities.mapCharactersToIntegers(activeLettersArray)
    val tokens            : List[List[Int]] = mappedCharacters
      .tokenize(equation)

    val columns : List[(Int, List[(Int, Int)])] = IterableUtilities.parse(tokens)
    val zeroMask: List[Boolean]                 = IterableUtilities.extractWordInitials(tokens)
                                                                   .buildZeroMask(activeLetterCount)

    ListUtilities.createCombinations(SingleDigitNumbers)(activeLetterCount)
                 .filter(canBeZero(zeroMask))
                 .find(columnSum(columns))
                 .map(toResultDictionary(activeLettersArray))
  }

  private def extractUnknowns(equation: String): Iterable[Char] =
    equation.filter(!" +=".contains(_))
            .distinct

  private def canBeZero(zeroMask: List[Boolean])(tokens: List[Int]): Boolean = {
    val found = tokens.indexWhere(_ == 0)
    found == -1 || zeroMask(found)
  }

  @tailrec
  private def columnSum(column: List[(Int, List[(Int, Int)])], carry: Int = 0)(permutation: List[Int]): Boolean =
    if (column.isEmpty) {
      carry == 0
    }
    else {
      val (y, xs) = column.head
      val sum     = calculatePermutationValue(carry, xs)(permutation)
      (permutation(y) == sum % 10) &&
      columnSum(column.tail, sum / 10)(permutation)
    }

  private def calculatePermutationValue(baseValue: Int, factorWeights: List[(Int, Int)])(permutation: List[Int]): Int =
    factorWeights.map(calculateFactorWeight(permutation)).sum + baseValue

  private def toResultDictionary(activeLetters: Array[Char])
                                (result: List[Int]): Map[Char, Int] =
    activeLetters.zip(result)
                 .toMap
}
