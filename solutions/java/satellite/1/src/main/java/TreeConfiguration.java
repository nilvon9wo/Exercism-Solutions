import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public record TreeConfiguration(
        List<Character> preorder,
        List<Character> inorderValues,
        int preorderStart,
        int preorderEnd,
        int inorderStart,
        int inorderEnd
) {
    public boolean isStartAfterEnd() {
        return preorderStart > preorderEnd;
    }

    public char getPreorderRoot() {
        return preorder.get(preorderStart);
    }

    public int getInorderStart() {
        return inorderStart;
    }

    public int getInorderRootIndex(char rootValue){
        return IntStream.rangeClosed(inorderStart, inorderEnd)
                        .filter(index -> inorderValues.get(index) == rootValue)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("traversals must have the same elements"));
    }

    public TreeConfiguration createLeft(int inorderRootIndex, int leftTreeSize) {
        return new TreeConfiguration(
                preorder,
                inorderValues,
                preorderStart + 1,
                preorderStart + leftTreeSize,
                inorderStart,
                inorderRootIndex - 1
        );
    }

    public TreeConfiguration createRight(int inorderRootIndex, int leftTreeSize) {
        return new TreeConfiguration(
                preorder,
                inorderValues,
                preorderStart + leftTreeSize + 1,
                preorderEnd,
                inorderRootIndex + 1,
                inorderEnd
        );
    }

    public static TreeConfiguration from(List<Character> preorderInput, List<Character> inorderInput) {
        validateTraversals(preorderInput, inorderInput);
        return new TreeConfiguration(preorderInput,
             inorderInput,
             0,
             preorderInput.size() - 1,
             0,
             inorderInput.size() - 1
        );
    }

    private static void validateTraversals(
            List<Character> preorder,
            List<Character> inorder
    ) {
        if (preorder.size() != inorder.size()) {
            throw new IllegalArgumentException(
                    "traversals must have the same length"
            );
        }

        if (hasDuplicates(preorder) || hasDuplicates(inorder)) {
            throw new IllegalArgumentException(
                    "traversals must contain unique items"
            );
        }

        if (!new HashSet<>(preorder).equals(new HashSet<>(inorder))) {
            throw new IllegalArgumentException(
                    "traversals must have the same elements"
            );
        }
    }

    private static boolean hasDuplicates(List<Character> values) {
        return values.size() != new HashSet<>(values).size();
    }
}
