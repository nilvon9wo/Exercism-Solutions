final class Element<T> {
    final T value;
    Element<T> prev;
    Element<T> next;

    Element(T value) {
        this.value = value;
    }
}