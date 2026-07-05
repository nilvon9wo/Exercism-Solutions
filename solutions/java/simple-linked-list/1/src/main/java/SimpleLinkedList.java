import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class SimpleLinkedList<T> {
    private Node<T> head;
    SimpleLinkedList() {
    }

    SimpleLinkedList(T[] values) {
        for (T value : values) {
            this.push(value);
        }
    }

    private int size;
    int size() {
        return this.size;
    }

    void push(T value) {
        this.head = new Node<>(value, head);
        this.size++;
    }

    T pop() {
        this.ensureNotEmpty();
        T value = this.head.getValue();
        this.head = this.head.getNext();
        this.size--;

        return value;
    }

    T peek() {
        this.ensureNotEmpty();
        return head.getValue();
    }

    void reverse() {
        Node<T> previous = null;
        Node<T> current = this.head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(previous);
            previous = current;
            current = next;
        }

        this.head = previous;
    }

    List<T> toList() {
        List<T> values = new ArrayList<>(size);
        Node<T> current = this.head;
        while (current != null) {
            values.add(current.getValue());
            current = current.getNext();
        }

        return values;
    }

    private void ensureNotEmpty() {
        if (head == null) {
            throw new NoSuchElementException();
        }
    }

}