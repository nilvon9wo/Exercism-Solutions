import java.util.Objects;

public class InputCell<T> implements Node<T> {
    InputCell(T initialValue) {
        this.cell = new Cell<>(initialValue);
    }

    private final Cell<T> cell;
    public T getValue() {
        return cell.value();
    }
    public void setValue(T value) {
        if (Objects.equals(cell.value(), value)) {
            return;
        }

        cell.update(value);
        React.propagate(cell.dependents());
    }

    @Override
    public Cell<T> cell() {
        return cell;
    }
}
