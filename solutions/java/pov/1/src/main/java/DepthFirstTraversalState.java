import java.util.*;

class DepthFirstTraversalState {

    Map<String, List<String>> graph;
    Map<String, String> parentMap;
    Set<String> visitedNodes;
    Deque<String> nodeStack;

    DepthFirstTraversalState(
            Map<String, List<String>> graph,
            Map<String, String> parentMap,
            String root
    ) {
        this.graph = graph;
        this.parentMap = parentMap;
        this.visitedNodes = initializeVisited(root);
        this.nodeStack = initializeStack(root);
    }

    private Set<String> initializeVisited(final String root) {
        Set<String> visited = new HashSet<>();
        visited.add(root);
        return visited;
    }

    private Deque<String> initializeStack(final String root) {
        Deque<String> stack = new ArrayDeque<>();
        stack.push(root);
        return stack;
    }
}