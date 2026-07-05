import java.util.*;

final class NodeFamilyParser {
    private static final Set<Character> OPTION_ENDING = Set.of(
            Symbol.GROUP_START.value(),
            Symbol.GROUP_END.value(),
            Symbol.PROPERTY_SEPARATOR.value()
    );

    public static Result<NodeFamily> parse(Input input) {
        if (input.current() != Symbol.PROPERTY_SEPARATOR.value()) {
            return Result.failure(new IllegalArgumentException("Each nodeFamily must begin with a separator."));
        }

        Result<SgfNode> parentResult = parseParent(input);
        if (!parentResult.isValid()) {
            return Result.failure(parentResult.getException());
        }

        Result<List<Group>> childrenResult = parseChildren(input);
        if (!childrenResult.isValid()) {
            return Result.failure(childrenResult.getException());
        }

        SgfNode parent = parentResult.getValue();
        List<Group> children = childrenResult.getValue();
        NodeFamily nodeFamily = new NodeFamily(parent, children);
        return Result.success(nodeFamily);
    }

    private static Result<SgfNode> parseParent(Input input) {
        Map<String, List<String>> parent = new HashMap<>();
        if (!input.moveNext()) {
            return Result.failure(new IllegalArgumentException("Unexpected parent ending."));
        }

        while (!isEndOfOptions(input)) {
            Result<KeyedOptions> keyedOptions = KeyedOptionsParser.parse(input);
            if (!keyedOptions.isValid()) {
                return Result.failure(keyedOptions.getException());
            }

            KeyedOptions value = keyedOptions.getValue();
            parent.put(value.key(), value.options());
        }

        return Result.success(new SgfNode(parent));
    }

    private static Result<List<Group>> parseChildren(Input input) {
        List<Group> children = new ArrayList<>();
        while (input.current() == Symbol.GROUP_START.value()) {
            Result<Group> childResult = GroupParser.parse(input);
            if (!childResult.isValid()) {
                return Result.failure(childResult.getException());
            }

            children.add(childResult.getValue());
            if (!input.moveNext()) {
                return Result.failure(
                        new IllegalArgumentException("Child Group ended unexpectedly.")
                );
            }
        }

        return Result.success(List.copyOf(children));
    }

    private static boolean isEndOfOptions(Input input) {
        return OPTION_ENDING.contains(input.current());
    }
}