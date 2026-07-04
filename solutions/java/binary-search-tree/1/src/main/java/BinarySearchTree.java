import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;
    void insert(T value) {
        this.root = this.insertInto(this.root, value);
    }

    private Node<T> insertInto(Node<T> current, T value) {
        if (current == null) {
            return new Node<>(value);
        }

        if (value.compareTo(current.data) <= 0) {
            current.left = this.insertInto(current.left, value);
        }
        else {
            current.right = this.insertInto(current.right, value);
        }

        return current;
    }

    List<T> getAsSortedList() {
        List<T> result = new ArrayList<>();
        this.inOrder(this.root, result);
        return result;
    }

    private void inOrder(Node<T> node, List<T> result) {
        if (node == null) {
            return;
        }

        this.inOrder(node.left, result);
        result.add(node.data);
        this.inOrder(node.right, result);
    }

    List<T> getAsLevelOrderList() {
        List<T> result = new ArrayList<>();
        if (this.root == null) {
            return result;
        }

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(this.root);

        while (!queue.isEmpty()) {
            Node<T> node = queue.poll();
            result.add(node.data);
            if (node.left != null) {
                queue.add(node.left);
            }

            if (node.right != null) {
                queue.add(node.right);
            }
        }

        return result;
    }

    Node<T> getRoot() {
        return this.root;
    }

    static class Node<T> {
        private final T data;
        private Node<T> left;
        private Node<T> right;

        Node(T data) {
            this.data = data;
        }

        Node<T> getLeft() {
            return this.left;
        }

        Node<T> getRight() {
            return this.right;
        }

        T getData() {
            return this.data;
        }
    }
}