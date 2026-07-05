import java.util.Arrays;
import java.util.List;

class ZebraPuzzle {

    private final List<Person> solution;

    public ZebraPuzzle() {

        Person[] candidates = PersonFactory.createAll()
                                           .filter(person ->
                                                           Arrays.stream(Constraints.HOUSE_RULES.toArray(new HouseRule[0]))
                                                                 .allMatch(rule -> Tester.test(person, rule))
                                           )
                                           .toArray(Person[]::new);

        this.solution = PersonSetFinder.findDistinctPersonSets(candidates)
                                       .filter(set ->
                                                       Arrays.stream(Constraints.NEIGHBOR_RULES.toArray(new NeighborRule[0]))
                                                             .allMatch(rule -> Tester.test(set, rule))
                                       )
                                       .findFirst()
                                       .orElseThrow()
                                       .stream()
                                       .toList();
    }

    public String getWaterDrinker() {
        return find(person -> person.drink() == Drink.Water)
                .nationality()
                .name();
    }

    public String getZebraOwner() {
        return find(person -> person.pet() == Pet.Zebra)
                .nationality()
                .name();
    }

    private Person find(java.util.function.Predicate<Person> predicate) {
        return solution.stream()
                       .filter(predicate)
                       .findFirst()
                       .orElseThrow();
    }
}