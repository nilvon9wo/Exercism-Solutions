import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Series {
    private final String string;

    Series(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("series cannot be empty");
        }

        this.string = string;
    }

    List<String> slices(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("slice length cannot be negative or zero");
        }

        if (length > string.length()) {
            throw new IllegalArgumentException("slice length cannot be greater than series length");
        }

        return IntStream.rangeClosed(0, string.length() - length)
                       .mapToObj(i -> string.substring(i, i + length))
                       .collect(Collectors.toList());
    }
}
