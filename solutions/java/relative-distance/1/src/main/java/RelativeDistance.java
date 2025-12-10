import java.util.*;
import java.util.stream.Stream;

record RelativeDistance(RelativeTree familyTree, BreadthFirstSearch breadthFirstSearch) {
    public RelativeDistance(Map<String, List<String>> familyMap) {
        this(new RelativeTree(familyMap), new BreadthFirstSearch());
    }

    int degreeOfSeparation(String personA, String personB) {
        return this.areSamePerson(personA, personB)
                       ? 0
                       : this.computeShortestDistance(personA, personB);
    }

    private boolean areSamePerson(String firstPerson, String secondPerson) {
        return firstPerson.equals(secondPerson);
    }

    private int computeShortestDistance(String startPerson, String targetPerson) {
        return this.breadthFirstSearch.findShortestDistance(
                startPerson,
                person -> person.equals(targetPerson),
                this::getUnvisitedNeighborsStream
        ).orElse(-1);
    }

    private Stream<String> getUnvisitedNeighborsStream(String person) {
        return familyTree.getNeighbors(person, new HashSet<>())
                       .stream();
    }
}
