final class Person(
                      val color: Color,
                      val nationality: Nationality,
                      val pet: Pet,
                      val drink: Drink,
                      val hobby: Hobby,
                      val house: House
                  ) {

    def noConflict(other: Person): Boolean = {
        require(other != null, "other")

        (this.color != other.color) &&
            (this.nationality != other.nationality) &&
            (this.pet != other.pet) &&
            (this.drink != other.drink) &&
            (this.hobby != other.hobby) &&
            (this.house != other.house)
    }
}