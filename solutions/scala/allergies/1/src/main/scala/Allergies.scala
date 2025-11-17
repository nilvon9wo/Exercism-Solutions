import Allergen.Allergen

object Allergen extends Enumeration {
  type Allergen = Value
  val Eggs: Allergen.Value = Value(1)
  val Peanuts: Allergen.Value = Value(2)
  val Shellfish: Allergen.Value = Value(4)
  val Strawberries: Allergen.Value = Value(8)
  val Tomatoes: Allergen.Value = Value(16)
  val Chocolate: Allergen.Value = Value(32)
  val Pollen: Allergen.Value = Value(64)
  val Cats: Allergen.Value = Value(128)
}

object Allergies {
  private val allergenValues: Allergen.ValueSet = Allergen.values
  private val allergens: List[Allergen] = allergenValues.toList.sortBy(_.id).reverse
  private val allergenTotal: Int = allergenValues.map(_.id).sum
  private val highestAssignedAllergenCode: Int = allergenValues.last.id

  @scala.annotation.tailrec
  def list(
            code: Int,
            toCheckAllergens: List[Allergen] = allergens,
            possessedAllergies: List[Allergen] = Nil
          ): List[Allergen] =
    code match {
      case 0 => possessedAllergies

      case code if code <= allergenTotal ⇒
        this.translateCode(code, toCheckAllergens, possessedAllergies)

      case _ ⇒
        this.list(this.ignoreExtraCode(code), toCheckAllergens, possessedAllergies)
    }

  def allergicTo(allergen: Allergen, code: Int): Boolean =
    this.list(code)
      .contains(allergen)

  private def ignoreExtraCode(code: Int): Int =
    this.ignoreExtraCode(code, this.highestPowerOf2(code))

  @scala.annotation.tailrec
  private def ignoreExtraCode(code: Int, highestCode: Int): Int =
    code match {
      case code if code <= allergenTotal ⇒
        code
      case _ ⇒
        ignoreExtraCode(code - highestCode: Int, highestCode / 2: Int)
    }

  @scala.annotation.tailrec
  private def highestPowerOf2(
                               number: Int,
                               lastAttempted: Int = highestAssignedAllergenCode
                             ): Int = {
    val nextAttempt = lastAttempted * 2
    if (nextAttempt > number) {
      lastAttempted
    }
    else {
      this.highestPowerOf2(number, nextAttempt)
    }
  }

  private def translateCode(
                             code: Int,
                             toCheckAllergens: List[Allergen],
                             possessedAllergies: List[Allergen]
                           ): List[Allergen] = {
    val allergen :: remainingAllergens = toCheckAllergens
    val allergenCode = allergen.id
    if (allergenCode <= code) {
      this.list(code - allergenCode, remainingAllergens, allergen :: possessedAllergies)
    }
    else {
      this.list(code, remainingAllergens, possessedAllergies)
    }
  }
}
