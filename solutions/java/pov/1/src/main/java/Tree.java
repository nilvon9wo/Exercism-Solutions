import java.util.*;

class Tree {
    final String label;
    final List<Tree> children;

    public Tree(String label) {
        this(label, new ArrayList<>());
    }

    public Tree(String label, List<Tree> children) {
        this.label = label;
        this.children = children;
    }

    public static Tree of(String label) {
        return new Tree(label);
    }

    public static Tree of(String label, List<Tree> children) {
        return new Tree(label, children);
    }

    public Tree fromPov(String fromNode) {
        return new TreePovService().fromPov(this, fromNode);
    }

    public List<String> pathTo(String fromNode, String toNode) {
        return new TreePathService().pathTo(this, fromNode, toNode);
    }

    @Override
    public boolean equals(Object o) {
        //noinspection SlowListContainsAll
        return this == o
               || o instanceof final Tree tree
                  && label.equals(tree.label)
                  && children.size() == tree.children.size()
                  && children.containsAll(tree.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, children);
    }

    @Override
    public String toString() {
        return "Tree{" + label + ", " + children + "}";
    }
}