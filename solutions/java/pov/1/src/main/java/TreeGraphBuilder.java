import java.util.*;

class TreeGraphBuilder {

    Map<String, List<String>> build(Tree root) {
        Map<String, List<String>> graph = new HashMap<>();
        buildEdges(root, null, graph);
        return graph;
    }

    private void buildEdges(Tree node, Tree parent, Map<String, List<String>> graph) {
        graph.computeIfAbsent(node.label, k -> new ArrayList<>());

        if (parent != null) {
            graph.get(node.label).add(parent.label);
            graph.get(parent.label).add(node.label);
        }

        for (Tree child : node.children) {
            buildEdges(child, node, graph);
        }
    }
}