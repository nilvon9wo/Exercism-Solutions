import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Cell<T> {
    private T value;
    private final Optional<ComputeCell<T>> computation;

    private Cell(
            T value,
            Optional<ComputeCell<T>> computation
    ) {
        this.value = value;
        this.computation = computation;
    }

    Cell(T value, ComputeCell<T> computation) {
        this(value, Optional.of(computation));
    }

    Cell(T value) {
        this(value, Optional.empty());
    }

    T value() {
        return value;
    }

    void update(T value) {
        this.value = value;
    }

    private final List<ComputeCell<T>> dependents = new ArrayList<>();
    void addDependent(ComputeCell<T> dependent) {
        dependents.add(dependent);
    }

    List<ComputeCell<T>> dependents() {
        return List.copyOf(dependents);
    }

    Optional<ComputeCell<T>> computation() {
        return computation;
    }
}