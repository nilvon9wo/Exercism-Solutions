import java.util.ArrayList;
import java.util.List;

final class GroupParser {
    static Result<Group> parse(Input input) {
        if (input.current() != Symbol.GROUP_START.value()) {
            return Result.failure(
                    new IllegalArgumentException("Input is missing group.")
            );
        }

        if (!input.moveNext()) {
            return Result.failure(
                    new IllegalArgumentException("Group is missing parent node.")
            );
        }

        List<NodeFamily> nodes = new ArrayList<>();
        while (input.current() != Symbol.GROUP_END.value()) {
            Result<NodeFamily> node = NodeFamilyParser.parse(input);
            if (node.isValid()) {
                nodes.add(node.getValue());
            }
            else {
                return Result.failure(node.getException());
            }
        }

        return nodes.isEmpty()
               ? Result.failure(new IllegalArgumentException("Input has no nodes."))
               : Result.success(new Group(nodes));
    }
}