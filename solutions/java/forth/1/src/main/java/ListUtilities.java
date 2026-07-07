import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;

public final class ListUtilities {
    public static <T> List<T> requireAtLeastOneValue(String operationName, List<T> values) {
        return requireAtLeastNValues(operationName, 1, values);
    }

    public static <T> List<T> requireAtLeastTwoValues(String operationName, List<T> values) {
        return requireAtLeastNValues(operationName, 2, values);
    }

    private static <T> List<T> requireAtLeastNValues(String operationName, int minimumCount, List<T> values
    ) {
        requireNotNull(values);
        String valueWord = (minimumCount == 1)
                                  ? "value"
                                  : "values";
        return requiresCount(
                values,
                count -> count < minimumCount,
                operationName + " requires that the stack contain at least "
                                            + minimumCount + " " + valueWord
                );
    }

    private static <T> List<T> requiresCount(
            List<T> values,
            IntPredicate validation,
            String errorMessage
    ) {
        if (validation.test(values.size())) {
            throw new IllegalArgumentException(errorMessage);
        }

        return values;
    }

    public static <T> T shift(List<T> values) {
        requireNotNull(values);
        return takeOne(
                values,
                list -> list.isEmpty()
                        ? null
                        : list.get(0)
        );
    }

    public static <T> T pop(List<T> values) {
        requireNotNull(values);
        return takeOne(
                values,
                list -> list.isEmpty()
                        ? null
                        : list.get(list.size() - 1)
        );
    }

    private static <T> T takeOne(
            List<T> values,
            Function<List<T>, T> function
    ) {
        T item = function.apply(values);
        if (item != null) {
            values.remove(item);
        }

        return item;
    }

    private static <T> void requireNotNull(List<T> values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
    }
}