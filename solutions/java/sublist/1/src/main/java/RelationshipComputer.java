import java.util.List;
import java.util.stream.IntStream;

class RelationshipComputer<T> {

    Relationship computeRelationship(List<T> firstList, List<T> secondList) {
        return firstList.equals(secondList)
               ? Relationship.EQUAL
               : this.isSublist(firstList, secondList)
                     ? Relationship.SUBLIST
                     : this.isSublist(secondList, firstList)
                           ? Relationship.SUPERLIST
                           : Relationship.UNEQUAL;

    }

    private boolean isSublist(List<T> candidateSublist, List<T> candidateSuperlist) {
        return candidateSublist.size() <= candidateSuperlist.size()
               && (
                    candidateSublist.isEmpty()
                        || this.containsSublist(candidateSublist, candidateSuperlist)
                );
    }

    private boolean containsSublist(List<T> candidateSublist, List<T> candidateSuperlist) {
        int lastPossibleStart = candidateSuperlist.size() - candidateSublist.size();
        return IntStream.rangeClosed(0, lastPossibleStart)
                        .anyMatch(startIndex
                                          -> this.matchesAt(candidateSublist, candidateSuperlist, startIndex)
                        );
    }

    private boolean matchesAt(
            List<T> candidateSublist,
            List<T> candidateSuperlist,
            int startIndex
    ) {
        return IntStream.range(0, candidateSublist.size())
                        .allMatch(offset -> {
                            final int superlistIndex = startIndex + offset;
                            final T sublistElement = candidateSublist.get(offset);
                            final T superlistElement = candidateSuperlist.get(superlistIndex);
                            return sublistElement.equals(superlistElement);
                        });
    }
}