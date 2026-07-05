public final class Node<T> {
    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    private final T value;
    public T getValue() {
        return value;
    }

    private Node<T> next;
    public Node<T> getNext() {
        return next;
    }
    @SuppressWarnings("UnusedReturnValue")
    public Node<T> setNext(Node<T> next) {
        this.next = next;
        return this;
    }
}