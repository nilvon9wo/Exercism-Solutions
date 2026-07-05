import java.util.stream.Stream;

final class PersonFactory {

    static Stream<Person> createAll() {
        return Stream.of(Color.values())
                     .flatMap(PersonFactory::createForColor);
    }

    private static Stream<Person> createForColor(Color color) {
        return Stream.of(Nationality.values())
                     .flatMap(nationality -> createForNationality(color, nationality));
    }

    private static Stream<Person> createForNationality(Color color, Nationality nationality) {
        return Stream.of(Pet.values())
                     .flatMap(pet -> createFor(color, nationality, pet));
    }

    private static Stream<Person> createFor(Color color, Nationality nationality, Pet pet) {
        return Stream.of(Drink.values())
                     .flatMap(drink -> createForDrink(color, nationality, pet, drink));
    }

    private static Stream<Person> createForDrink(
            Color color,
            Nationality nationality,
            Pet pet,
            Drink drink
    ) {
        return Stream.of(Hobby.values())
                     .flatMap(hobby -> createHousePermutations(color, nationality, pet, drink, hobby));
    }

    private static Stream<Person> createHousePermutations(
            Color color,
            Nationality nationality,
            Pet pet,
            Drink drink,
            Hobby hobby
    ) {
        return Stream.of(House.values())
                     .map(house -> new Person(
                             color,
                             nationality,
                             pet,
                             drink,
                             hobby,
                             house
                     ));
    }
}