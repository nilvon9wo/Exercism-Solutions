import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RelativeTree {

    private final Map<String, Set<String>> parentToChildren;
    private final Map<String, Set<String>> childToParents;

    public RelativeTree(Map<String, List<String>> familyTree) {
        this.parentToChildren = this.buildParentToChildrenMap(familyTree);
        this.childToParents = this.buildChildToParentsMap(familyTree);
    }

    private Map<String, Set<String>> buildParentToChildrenMap(Map<String, List<String>> familyTree) {
        return familyTree.entrySet()
                       .stream()
                       .collect(this.collectParentToChildren());
    }

    private Collector<Map.Entry<String, List<String>>, ?, Map<String, Set<String>>> collectParentToChildren() {
        return Collectors.toMap(Map.Entry::getKey, this::convertChildListToSet);
    }

    private HashSet<String> convertChildListToSet(Map.Entry<String, List<String>> entry) {
        return new HashSet<>(entry.getValue());
    }

    private Map<String, Set<String>> buildChildToParentsMap(Map<String, List<String>> familyTree) {
        return familyTree.entrySet()
                       .stream()
                       .flatMap(this::flattenParentChildPairs)
                       .collect(this.collectChildToParents());
    }

    private Stream<Map.Entry<String, String>> flattenParentChildPairs(Map.Entry<String, List<String>> entry) {
        return entry.getValue()
                       .stream()
                       .map(child -> this.createChildToParentEntry(entry, child));
    }

    private Map.Entry<String, String> createChildToParentEntry(Map.Entry<String, List<String>> entry, String child) {
        return Map.entry(child, entry.getKey());
    }

    private Collector<Map.Entry<String, String>, ?, Map<String, Set<String>>> collectChildToParents() {
        Collector<Map.Entry<String, String>, ?, Set<String>> parentSetCollector =
                Collectors.mapping(Map.Entry::getValue, Collectors.toSet());
        return Collectors.groupingBy(Map.Entry::getKey, parentSetCollector);
    }

    public Set<String> getNeighbors(String person, Set<String> visitedPersons) {
        Set<String> neighbors = this.collectNeighbors(person);
        return this.filterVisitedNeighbors(visitedPersons, neighbors);
    }

    private Set<String> collectNeighbors(String person) {
        Stream<String> childrenStream = this.streamFromMap(this.parentToChildren, person);
        Stream<String> parentsStream = this.streamFromMap(this.childToParents, person);
        Stream<String> directRelationsStream = Stream.concat(childrenStream, parentsStream);
        Stream<String> siblingStream = this.streamSiblings(person);
        return Stream.concat(directRelationsStream, siblingStream)
                       .collect(Collectors.toSet());
    }

    private Stream<String> streamSiblings(String person) {
        return this.streamFromMap(this.childToParents, person)
                       .flatMap(parent -> this.streamFromMap(this.parentToChildren, parent))
                       .filter(sibling -> !sibling.equals(person));
    }

    private Stream<String> streamFromMap(Map<String, Set<String>> map, String key) {
        return map.getOrDefault(key, Set.of())
                       .stream();
    }

    private Set<String> filterVisitedNeighbors(Set<String> visitedPersons, Set<String> neighbors) {
        return neighbors.stream()
                       .filter(neighbor -> !visitedPersons.contains(neighbor))
                       .collect(Collectors.toSet());
    }
}
