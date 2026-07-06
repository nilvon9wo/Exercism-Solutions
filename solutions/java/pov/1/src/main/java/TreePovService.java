import java.util.*;

class TreePovService {
    public Tree fromPov(Tree root, String fromNode) {
        Map<String, List<String>> graph = new TreeGraphBuilder()
                .build(root);

        if (!graph.containsKey(fromNode)) {
            throw new UnsupportedOperationException("Tree could not be reoriented");
        }

        Map<String, String> parentMap = new HashMap<>();
        buildParentMap(new DepthFirstTraversalState(graph, parentMap, fromNode));

        return buildTree(new GraphTraversalState(graph, parentMap, null, fromNode));
    }

    private void buildParentMap(DepthFirstTraversalState state) {
        while (!state.nodeStack.isEmpty()) {
            processDepthFirstStep(state);
        }
    }

    private void processDepthFirstStep(DepthFirstTraversalState state) {
        String node = state.nodeStack.pop();

        for (String neighbor : state.graph.get(node)) {
            if (!state.visitedNodes.contains(neighbor)) {
                state.visitedNodes.add(neighbor);
                state.parentMap.put(neighbor, node);
                state.nodeStack.push(neighbor);
            }
        }
    }

    private Tree buildTree(GraphTraversalState state) {
        List<Tree> children =
                state.getAdjacentNodes()
                     .stream()
                     .filter(neighbor -> !neighbor.equals(state.parentNode))
                     .filter(state::isNotParentNode)
                     .map(neighbor -> buildTree(state.createFor(neighbor)))
                     .collect(java.util.stream.Collectors.toList());

        return new Tree(state.currentNode, children);
    }
}