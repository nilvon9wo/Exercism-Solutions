import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class NodeFamily {

    private final SgfNode parent;
    private final List<Group> children;

    public NodeFamily(SgfNode parent, List<Group> children) {
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }

        this.parent = parent;
        this.children = (children == null) ? List.of() : List.copyOf(children);
    }

    public SgfNode getParent() {
        return parent;
    }

    public List<Group> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public SgfNode toTree() {
        Map<String, List<String>> parentCopy = SgfParentCopier.copy(this.parent);
        if (children.isEmpty()) {
            return new SgfNode(parentCopy, new ArrayList<>());
        }

        List<SgfNode> childTrees = children.stream()
                                           .map(Group::toTree)
                                           .toList();
        return new SgfNode(parentCopy,childTrees);
    }
}