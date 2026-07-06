import java.util.*;

class GraphTraversal {

    Map<String, String> previous = new HashMap<>();
    Set<String> visited;
    Deque<String> queue;
    boolean found = false;

    GraphTraversal(String start) {
        this.visited = initializeVisited(start);
        this.queue = initializeQueue(start);
    }

    private Set<String> initializeVisited(final String start) {
        Set<String> visited = new HashSet<>();
        visited.add(start);
        return visited;
    }

    private Deque<String> initializeQueue(final String start) {
        Deque<String> queue = new ArrayDeque<>();
        queue.add(start);
        return queue;
    }

    public boolean processGraphUntilTargetFound(
            final Map<String, List<String>> graph,
            final String target
    ) {
        String node = this.queue.poll();

        if (Objects.equals(node, target)) {
            this.found = true;
            return true;
        } else {
            for (String neighbor : graph.get(node)) {
                this.enqueue(neighbor, node);
            }
        }

        return false;
    }

    private void enqueue(final String neighbor, final String node) {
        if (!this.visited.contains(neighbor)) {
            this.visited.add(neighbor);
            this.previous.put(neighbor, node);
            this.queue.add(neighbor);
        }
    }
}