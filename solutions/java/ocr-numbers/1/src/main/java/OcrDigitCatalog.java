import java.util.List;
import java.util.stream.IntStream;

final class OcrDigitCatalog {

    private static final List<DigitPattern> DIGITS = List.of(
            new DigitPattern(new String[]{
                    " _ ",
                    "| |",
                    "|_|",
                    "   "
            }, "0"),

            new DigitPattern(new String[]{
                    "   ",
                    "  |",
                    "  |",
                    "   "
            }, "1"),

            new DigitPattern(new String[]{
                    " _ ",
                    " _|",
                    "|_ ",
                    "   "
            }, "2"),

            new DigitPattern(new String[]{
                    " _ ",
                    " _|",
                    " _|",
                    "   "
            }, "3"),

            new DigitPattern(new String[]{
                    "   ",
                    "|_|",
                    "  |",
                    "   "
            }, "4"),

            new DigitPattern(new String[]{
                    " _ ",
                    "|_ ",
                    " _|",
                    "   "
            }, "5"),

            new DigitPattern(new String[]{
                    " _ ",
                    "|_ ",
                    "|_|",
                    "   "
            }, "6"),

            new DigitPattern(new String[]{
                    " _ ",
                    "  |",
                    "  |",
                    "   "
            }, "7"),

            new DigitPattern(new String[]{
                    " _ ",
                    "|_|",
                    "|_|",
                    "   "
            }, "8"),

            new DigitPattern(new String[]{
                    " _ ",
                    "|_|",
                    " _|",
                    "   "
            }, "9")
    );

    String match(String[] candidate) {
        return DIGITS.stream()
                     .filter(pattern -> matches(pattern.rows(), candidate))
                     .findFirst()
                     .map(DigitPattern::value)
                     .orElse("?");
    }

    private boolean matches(String[] a, String[] b) {
        return IntStream.range(0, 4)
                        .allMatch(i -> a[i].equals(b[i]));
    }
}