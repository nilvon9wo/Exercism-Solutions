import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GottaSnatchEmAll {

    static Set<String> newCollection(List<String> cards) {
		return new HashSet<>(cards);
    }

    @SuppressWarnings("SameParameterValue")
    static boolean addCard(String card, Set<String> collection) {
		return collection.add(card);
    }

	static boolean canTrade(Set<String> myCollection, Set<String> theirCollection) {
		return hasElementNotIn(myCollection, theirCollection)
				       && hasElementNotIn(theirCollection, myCollection);
	}

	private static boolean hasElementNotIn(Set<String> source, Set<String> target) {
		return source.stream()
				       .anyMatch(e -> !target.contains(e));
	}

    static Set<String> commonCards(List<Set<String>> collections) {
        return collections.stream()
		               .reduce((set1, set2) -> {
			               Set<String> copy = new HashSet<>(set1);
			               copy.retainAll(set2);
			               return copy;
		               })
		               .orElse(Collections.emptySet());

    }

    static Set<String> allCards(List<Set<String>> collections) {
        return collections.stream()
		               .flatMap(Set::stream)
		               .collect(Collectors.toSet());
    }
}
