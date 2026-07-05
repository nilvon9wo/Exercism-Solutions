import java.util.stream.IntStream;

public final class StringUtilities {
    public static boolean containsLowerCase(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value");
        }

        return IntStream.range(0, value.length())
                        .anyMatch(i -> Character.isLowerCase(value.charAt(i)));
    }
}