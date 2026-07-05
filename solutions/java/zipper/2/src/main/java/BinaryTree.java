record BinaryTree(Zipper root) {

    BinaryTree(int value) {
        this(new Zipper(value));
    }

    BinaryTree(Zipper root) {
        this.root = root;
        this.root.rootTree = this;
    }

    String printTree() {
        return printNode(root);
    }

    private String printNode(Zipper node) {
        if (node == null) {
            return "null";
        }

        String leftPart = node.left == null
                          ? "null"
                          : "{ " + printNode(node.left) + " }";

        String rightPart = node.right == null
                           ? "null"
                           : "{ " + printNode(node.right) + " }";

        return "value: " + node.value + ", left: " + leftPart + ", right: " + rightPart;
    }

    public Zipper getRoot() {
        return this.root;
    }
}