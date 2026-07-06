import java.util.*;

class GraphTraversalState {

    Map<String, List<String>> graph;
    Map<String, String> parentMap;
    String parentNode;
    String currentNode;

    GraphTraversalState(
            Map<String, List<String>> graph,
            Map<String, String> parentMap,
            String parentNode,
            String currentNode
    ) {
        this.graph = graph;
        this.parentMap = parentMap;
        this.parentNode = parentNode;
        this.currentNode = currentNode;
    }

    List<String> getAdjacentNodes() {
        return this.graph.get(this.currentNode);
    }

    boolean isNotParentNode(final String neighbor) {
        return !this.parentMap.containsKey(this.currentNode)
               || !this.parentMap.get(this.currentNode).equals(neighbor);
    }

    GraphTraversalState createFor(String neighborNode) {
        return new GraphTraversalState(
                this.graph,
                this.parentMap,
                this.parentNode,
                neighborNode
        );
    }
}