import java.util.*;

class TreePathService {

    public List<String> pathTo(Tree root, String fromNode, String toNode) {
        Map<String, List<String>> graph = new TreeGraphBuilder()
                .build(root);

        if (!graph.containsKey(fromNode) || !graph.containsKey(toNode)) {
            throw new UnsupportedOperationException("No path found");
        }

        return bfsPath(graph, fromNode, toNode);
    }

    private List<String> bfsPath(
            Map<String, List<String>> graph,
            String start,
            String target
    ) {
        GraphTraversal graphTraversal = new GraphTraversal(start);

        while (!graphTraversal.queue.isEmpty()) {
            if (graphTraversal.processGraphUntilTargetFound(graph, target)) {
                break;
            }
        }

        if (!graphTraversal.found) {
            throw new UnsupportedOperationException("No path found");
        }

        return reconstructPath(target, graphTraversal.previous);
    }

    private List<String> reconstructPath(String target, Map<String, String> prev) {
        List<String> path = new ArrayList<>();
        String current = target;

        while (current != null) {
            path.add(current);
            current = prev.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}