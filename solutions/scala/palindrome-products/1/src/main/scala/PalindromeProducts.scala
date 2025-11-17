import scala.collection.mutable

class PalindromeProducts(lowerLimit: Int, upperLimit: Int) {
  type Factors = (Int, Int)
  type ProductWithFactorSet = (Int, Set[Factors])
  private val range: Range.Inclusive = {
    this.lowerLimit to this.upperLimit
  }

  def smallest: Option[ProductWithFactorSet] =
    this.find(this.findSmallestPalindromeProduct)

  private def findSmallestPalindromeProduct: Option[Int] =
    this.findTargetPalindromeProduct(this.isSmallestFoundPalindrome)

  private def isSmallestFoundPalindrome(smallestFoundPalindrome: Option[Int], candidate: Int): Boolean =
    this.couldBeTargetPalindrome(smallestFoundPalindrome, candidate, _ < _)

  private def couldBeTargetPalindrome(
                                       foundPalindrome: Option[Int],
                                       candidate: Int,
                                       comparison: (Int, Int) ⇒ Boolean
                                     ): Boolean =
    foundPalindrome.isEmpty ||
      comparison(candidate, foundPalindrome.get)

  private def findTargetPalindromeProduct(
                                           couldBeTargetPalindrome: (Option[Int], Int) ⇒ Boolean
                                         ): Option[Int] = {
    var palindromeProduct: Option[Int] = None
    this.range.foreach {
      p ⇒
        this.range.foreach {
          q ⇒
            palindromeProduct =
              this.findTargetPalindromeProduct(palindromeProduct, p, couldBeTargetPalindrome)(q)
        }
    }
    palindromeProduct
  }

  private def findTargetPalindromeProduct(
                                           oldPalindromeProduct: Option[Int],
                                           p: Int,
                                           couldBeTargetPalindrome: (Option[Int], Int) ⇒ Boolean
                                         )(q: Int): Option[Int] = {
    val newCandidateProduct = p * q
    if (
      isPalindrome(newCandidateProduct)
        && couldBeTargetPalindrome(oldPalindromeProduct, newCandidateProduct)
    ) {
      Some(newCandidateProduct)
    }
    else {
      oldPalindromeProduct
    }
  }

  private def isPalindrome(candidate: Int): Boolean = {
    val candidateString = candidate.toString
    candidateString == candidateString.reverse
  }

  private def find(palindromeProduct: Option[Int]): Option[ProductWithFactorSet] = {
    if (
      this.lowerLimit < this.upperLimit
        && palindromeProduct.isDefined
    ) {
      Some(
        palindromeProduct.get,
        this.findFactors(palindromeProduct.get)
      )
    }
    else {
      None
    }
  }

  private def findFactors(dividend: Int): Set[Factors] = {
    val factors = mutable.Set[Factors]()

    def addToFactors(factor1: Int, factor2: Int): Unit = {
      if (factor1 < factor2) {
        factors.add(factor1, factor2)
      }
      else {
        factors.add(factor2, factor1)
      }
    }

    this.range.foreach {
      devisor ⇒
        if (dividend % devisor == 0) {
          val quotient = dividend / devisor
          if (this.range.contains(quotient)) {
            addToFactors(devisor, quotient)
          }
        }
    }
    factors.toSet
  }

  def largest: Option[ProductWithFactorSet] =
    this.find(this.findLargestPalindromeProduct)

  private def findLargestPalindromeProduct: Option[Int] =
    this.findTargetPalindromeProduct(this.isLargestFoundPalindrome)

  private def isLargestFoundPalindrome(largestFoundPalindrome: Option[Int], candidate: Int): Boolean =
    this.couldBeTargetPalindrome(largestFoundPalindrome, candidate, _ > _)
}

object PalindromeProducts {
  def apply(smallest: Int, largest: Int) =
    new PalindromeProducts(smallest, largest)
}