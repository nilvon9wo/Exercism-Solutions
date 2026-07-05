import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ListOps {
    private ListOps() {
    }

    static <T> List<T> append(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    static <T> List<T> concat(List<List<T>> listOfLists) {
        return listOfLists.stream()
                          .flatMap(Collection::stream)
                          .collect(Collectors.toList());
    }

    static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
                   .filter(predicate)
                   .collect(Collectors.toList());
    }

    static <T> int size(List<T> list) {
        return list.size();
    }

    static <T, U> List<U> map(List<T> list, Function<T, U> transform) {
        return list.stream()
                   .map(transform)
                   .collect(Collectors.toList());
    }

    static <T> List<T> reverse(List<T> list) {
        return IntStream.iterate(list.size() - 1, i -> i >= 0, i -> i - 1)
                        .mapToObj(list::get)
                        .collect(Collectors.toCollection(() -> new ArrayList<>(list.size())));
    }

    static <T, U> U foldLeft(List<T> list, U initial, BiFunction<U, T, U> function) {
        U accumulator = initial;
        for (T element : list) {
            accumulator = function.apply(accumulator, element);
        }

        return accumulator;
    }

    static <T, U> U foldRight(List<T> list, U initial, BiFunction<T, U, U> function) {
        U accumulator = initial;
        for (int i = list.size() - 1; i >= 0; i--) {
            accumulator = function.apply(list.get(i), accumulator);
        }

        return accumulator;
    }
}