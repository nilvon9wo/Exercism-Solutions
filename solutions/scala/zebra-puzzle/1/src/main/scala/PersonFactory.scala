object PersonFactory {

    def createAll(): Iterator[Person] =
        createRange()
            .flatMap(createForColor)
            .map(identity)

    private def createForColor(color: Int): Iterator[Person] =
        createRange()
            .flatMap(nationality => createForNationality(color, nationality))

    private def createForNationality(color: Int, nationality: Int): Iterator[Person] =
        createRange()
            .flatMap(pet => createFor(color, nationality, pet))

    private def createFor(color: Int, nationality: Int, pet: Int): Iterator[Person] =
        createRange()
            .flatMap(drink => createForDrink(color, nationality, pet, drink))

    private def createForDrink(
                                  color: Int,
                                  nationality: Int,
                                  pet: Int,
                                  drink: Int
                              ): Iterator[Person] =
        createRange()
            .flatMap(hobby =>
                createHousePermutations(color, nationality, pet, drink, hobby)
            )

    private def createHousePermutations(
                                           color: Int,
                                           nationality: Int,
                                           pet: Int,
                                           drink: Int,
                                           hobby: Int
                                       ): Iterator[Person] =
        createRange()
            .map(house =>
                new Person(
                    Color.fromOrdinal(color),
                    Nationality.fromOrdinal(nationality),
                    Pet.fromOrdinal(pet),
                    Drink.fromOrdinal(drink),
                    Hobby.fromOrdinal(hobby),
                    House.fromOrdinal(house)
                )
            )

    private def createRange(): Iterator[Int] =
        Iterator.range(0, 5)
}