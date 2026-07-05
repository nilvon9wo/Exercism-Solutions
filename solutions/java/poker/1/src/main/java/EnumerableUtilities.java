import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EnumerableUtilities {
    public static <T, K> K findTripletKey(
            Iterable<T> items,
            Function<T, K> groupKeySelector
    ) {
        return findGroupKey(
                items,
                groupKeySelector,
                group -> group.size() >= 3
        );
    }

    private static <T, K> K findGroupKey(
            Iterable<T> items,
            Function<T, K> groupKeySelector,
            Predicate<List<T>> condition
    ) {
        Map<K, List<T>> groupedItems =
                StreamSupport.stream(items.spliterator(), false)
                             .collect(Collectors.groupingBy(groupKeySelector));
        return groupedItems.entrySet()
                           .stream()
                           .filter(entry -> condition.test(entry.getValue()))
                           .map(Map.Entry::getKey)
                           .findFirst()
                           .orElse(null);
    }
}
