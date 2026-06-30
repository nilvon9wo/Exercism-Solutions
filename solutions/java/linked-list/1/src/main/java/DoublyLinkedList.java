class DoublyLinkedList<T> {
    private Element<T> head;
    private Element<T> tail;

    void push(T value) {
        insert(new Element<>(value), End.TAIL);
    }

    void unshift(T value) {
        insert(new Element<>(value), End.HEAD);
    }

    private void insert(Element<T> node, End end) {
        if (isEmpty()) {
            setSingle(node);
            return;
        }

        if (end == End.HEAD) {
            linkBeforeHead(node);
        }
        else {
            linkAfterTail(node);
        }
    }

    private void linkBeforeHead(Element<T> node) {
        node.next = head;
        head.prev = node;
        head = node;
    }

    private void linkAfterTail(Element<T> node) {
        tail.next = node;
        node.prev = tail;
        tail = node;
    }

    T pop() {
        return remove(End.TAIL);
    }

    T shift() {
        return remove(End.HEAD);
    }

    private T remove(End end) {
        if (isEmpty()) {
            return null;
        }

        Element<T> removed;

        if (end == End.HEAD) {
            removed = head;
            unlinkHead();
        }
        else {
            removed = tail;
            unlinkTail();
        }

        return removed.value;
    }

    private void unlinkHead() {
        if (isSingleElement()) {
            clear();
            return;
        }

        head = head.next;
        head.prev = null;
    }

    private void unlinkTail() {
        if (isSingleElement()) {
            clear();
            return;
        }

        tail = tail.prev;
        tail.next = null;
    }

    private boolean isSingleElement() {
        return head == tail;
    }

    private boolean isEmpty() {
        return head == null;
    }

    private void setSingle(Element<T> node) {
        head = node;
        tail = node;
    }

    private void clear() {
        head = null;
        tail = null;
    }
}