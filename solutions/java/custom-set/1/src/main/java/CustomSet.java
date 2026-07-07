import java.util.*;
import java.util.stream.Collectors;

class CustomSet<T> {
    private final List<T> elements;
    CustomSet() {
        this.elements = new ArrayList<>();
    }

    CustomSet(Collection<T> data) {
        this();
        data.forEach(this::add);
    }

    boolean isEmpty() {
        return elements.isEmpty();
    }

    boolean contains(T element) {
        return elements.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        return elements.stream()
                       .noneMatch(other::contains);
    }

    void add(T element) {
        if (contains(element)) {
            return;
        }

        elements.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        //noinspection SuspiciousMethodCalls
        return this == obj
               || obj instanceof CustomSet<?> other
                    && elements.size() == other.elements.size()
                    && new HashSet<>(other.elements).containsAll(elements);
    }

    @Override
    public int hashCode() {
        return elements.stream()
                       .map(Objects::hashCode)
                       .sorted()
                       .reduce(1, (hash, value) -> 31 * hash + value);
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        return new CustomSet<>(
                elements.stream()
                        .filter(other::contains)
                        .collect(Collectors.toList())
        );
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        CustomSet<T> union = new CustomSet<>(elements);
        other.elements.forEach(union::add);
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        return new CustomSet<>(
                elements.stream()
                        .filter(element -> !other.contains(element))
                        .collect(Collectors.toList())
        );
    }

    boolean isSubset(CustomSet<T> other) {
        return other.elements.stream()
                             .allMatch(this::contains);
    }
}