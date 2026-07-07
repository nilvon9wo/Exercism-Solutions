import java.util.List;

public class Satellite {

    Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        TreeConfiguration config = TreeConfiguration.from(preorderInput, inorderInput);
        Node root = buildTree(config);
        return new Tree(root);
    }

    private Node buildTree(
            TreeConfiguration config
    ) {
        if (config.isStartAfterEnd()) {
            return null;
        }

        char rootValue = config.getPreorderRoot();
        int inorderRootIndex = config.getInorderRootIndex(rootValue);
        int leftTreeSize = inorderRootIndex - config.getInorderStart();

        Node root = new Node(rootValue);
        root.left = buildTree(config.createLeft(inorderRootIndex, leftTreeSize));
        root.right = buildTree(config.createRight(inorderRootIndex, leftTreeSize));
        return root;
    }
}