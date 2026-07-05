import java.util.*;

final class Tester {

    static boolean applies(Person person, Object aspect) {
        if (aspect instanceof Color color) {
            return person.color() == color;
        }
        if (aspect instanceof Nationality nationality) {
            return person.nationality() == nationality;
        }
        if (aspect instanceof Pet pet) {
            return person.pet() == pet;
        }
        if (aspect instanceof Drink drink) {
            return person.drink() == drink;
        }
        if (aspect instanceof Hobby hobby) {
            return person.hobby() == hobby;
        }
        return person.house() == aspect;
    }

    static boolean test(Person person, HouseRule rule) {
        boolean subjectMatches = applies(person, rule.subject());
        boolean factMatches = applies(person, rule.fact());

        if (subjectMatches) {
            return rule.isTrue() == factMatches;
        } else {
            return !rule.isTrue()
                   || !factMatches;
        }
    }

    static boolean test(Set<Person> set, NeighborRule rule) {
        List<Person> sorted = new ArrayList<>(set);
        sorted.sort(Comparator.comparing(Person::house));

        for (int i = 0; i < sorted.size() - 1; i++) {
            Person left = sorted.get(i);
            Person right = sorted.get(i + 1);

            if (rule.relation() == Neighbor.LeftOf) {
                if (applies(left, rule.subject()) && applies(right, rule.fact())) {
                    return true;
                }
            }

            if (rule.relation() == Neighbor.RightOf) {
                if (applies(right, rule.subject()) && applies(left, rule.fact())) {
                    return true;
                }
            }
        }

        return rule.relation() == Neighbor.Either
               && isSatisfiedByAdjacentHouses(set, rule);
    }

    private static boolean isSatisfiedByAdjacentHouses(final Set<Person> set, final NeighborRule rule) {
        return test(set, new NeighborRule(rule.subject(), rule.fact(), Neighbor.LeftOf))
               || test(set, new NeighborRule(rule.subject(), rule.fact(), Neighbor.RightOf));
    }
}