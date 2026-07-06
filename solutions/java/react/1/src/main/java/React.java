import java.util.*;
import java.util.function.Function;

public class React {
    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    static <T> ComputeCell<T> computeCell(
            Function<List<T>, T> function,
            List<? extends Node<T>> cells
    ) {
        List<Cell<T>> dependencies = cells.stream()
                                          .map(Node::cell)
                                          .toList();

        return new ComputeCell<>(function, dependencies);
    }

    public static <T> void propagate(List<ComputeCell<T>> initialCells) {
        Set<ComputeCell<T>> affected = new HashSet<>();
        collectAffected(initialCells, affected);
        affected.stream()
                .sorted(Comparator.comparingInt(React::depth))
                .forEach(ComputeCell::update);
    }

    private static <T> void collectAffected(
            List<ComputeCell<T>> cells,
            Set<ComputeCell<T>> affected
    ) {
        cells.forEach(cell -> {
            if (!affected.add(cell)) {
                return;
            }

            collectAffected(cell.dependents(), affected);
        });
    }

    private static <T> int depth(ComputeCell<T> cell) {
        return cell.dependencies.stream()
                                .map(Cell::computation)
                                .flatMap(Optional::stream)
                                .mapToInt(React::depth)
                                .max()
                                .orElse(0) + 1;
    }
}