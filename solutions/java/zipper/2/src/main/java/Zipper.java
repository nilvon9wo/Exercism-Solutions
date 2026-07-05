class Zipper {
    Zipper up;
    Zipper left;
    Zipper right;
    public int value;
    public BinaryTree rootTree;

    Zipper(int val) {
        this.value = val;
    }

    int getValue() {
        return value;
    }

    @SuppressWarnings("SameParameterValue")
    void setValue(int value) {
        this.value = value;
    }

    @SuppressWarnings("UnusedReturnValue")
    Zipper setLeft(Zipper leftChild) {
        this.left = leftChild;

        if (leftChild != null) {
            leftChild.up = this;
            leftChild.rootTree = this.rootTree;
        }

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    Zipper setRight(Zipper rightChild) {
        this.right = rightChild;

        if (rightChild != null) {
            rightChild.up = this;
            rightChild.rootTree = this.rootTree;
        }

        return this;
    }

    BinaryTree toTree() {
        Zipper node = this;

        while (node.up != null) {
            node = node.up;
        }

        return node.rootTree;
    }
}