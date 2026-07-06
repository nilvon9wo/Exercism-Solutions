import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ComputeCell<T> implements Node<T> {
    private final Function<List<T>, T> function;
    final List<Cell<T>> dependencies;

    ComputeCell(
            Function<List<T>, T> function,
            List<Cell<T>> dependencies
    ) {
        this.function = function;
        this.dependencies = dependencies;
        this.cell = new Cell<>(calculate(), this);
        dependencies.forEach(dependency -> dependency.addDependent(this));
    }

    private final List<Consumer<T>> callbacks = new ArrayList<>();
    public void addCallback(Consumer<T> callback) {
        callbacks.add(callback);
    }
    public void removeCallback(Consumer<T> callback) {
        callbacks.remove(callback);
    }

    private final Cell<T> cell;
    public T getValue() {
        return cell.value();
    }

    public void update() {
        T oldValue = cell.value();
        T newValue = calculate();

        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        cell.update(newValue);
        callbacks.forEach(callback -> callback.accept(newValue));
    }

    private T calculate() {
        return function.apply(dependencies.stream()
                                          .map(Cell::value)
                                          .toList());
    }

    public List<ComputeCell<T>> dependents() {
        return cell.dependents();
    }

    @Override
    public Cell<T> cell() {
        return cell;
    }
}
