import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record BreadthContext<NodeType>(
        List<BreadthNodeStep<NodeType>> currentFrontier,
        Set<NodeType> visitedNodes
) {
    public static <NodeType> BreadthContext<NodeType> createFrom(NodeType startNode) {
        List<BreadthNodeStep<NodeType>> currentFrontier = BreadthNodeStep.createListFrom(startNode);
        Set<NodeType> visitedNodes = new HashSet<>();
        return new BreadthContext<>(currentFrontier, visitedNodes);
    }
}
