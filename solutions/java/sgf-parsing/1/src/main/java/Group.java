import java.util.List;
import java.util.Map;

public final class Group {
    private final List<NodeFamily> nodes;

    public Group(List<NodeFamily> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("nodes cannot be null or empty");
        }

        this.nodes = List.copyOf(nodes);
    }

    public SgfNode toTree() {
        NodeFamily parentNodeFamily = nodes.get(0);
        if (parentNodeFamily.hasChildren()) {
            return parentNodeFamily.toTree();
        }

        SgfNode[] children = nodes.stream()
                                  .skip(1)
                                  .map(NodeFamily::toTree)
                                  .toArray(SgfNode[]::new);

        Map<String, List<String>> parentCopy = SgfParentCopier.copy(parentNodeFamily.getParent());
        return new SgfNode(parentCopy, List.of(children));
    }
}