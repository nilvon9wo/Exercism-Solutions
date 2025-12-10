import java.util.List;

public record BreadthNodeStep<NodeType>(NodeType node, int distance) {
    public BreadthNodeStep<NodeType> advanceToNeighbor(NodeType neighbor) {
        return new BreadthNodeStep<>(neighbor, this.distance() + 1);
    }

    public static <NodeType> List<BreadthNodeStep<NodeType>> createListFrom(NodeType startNode) {
        BreadthNodeStep<NodeType> step = new BreadthNodeStep<>(startNode, 0);
        return List.of(step);
    }
}