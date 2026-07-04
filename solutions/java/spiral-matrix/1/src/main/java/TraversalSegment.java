public record TraversalSegment(
        TraversalAxis axis,
        int fixedIndex,
        int from,
        int to,
        int step
) {
}