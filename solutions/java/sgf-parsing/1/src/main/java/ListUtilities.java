import java.util.List;
import java.util.Objects;

public final class ListUtilities {
    public static <T> T shift(List<T> list) {
        Objects.requireNonNull(list, "list");

        if (list.isEmpty()) {
            throw new IllegalStateException("The list is empty.");
        }

        return list.remove(0);
    }
}