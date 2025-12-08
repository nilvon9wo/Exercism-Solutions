import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GrepFlag {
    IGNORE_CASE("-i"),
    INCLUDE_LINE_NUMBERS("-n"),
    INCLUDE_FILE_NAMES("-l"),
    MATCH_ENTIRE_LINE("-x"),
    INVERT("-v");

    private final String code;
    GrepFlag(String code) {
        this.code = code;
    }

    static EnumSet<GrepFlag> fromStrings(List<String> flags) {
        return flags.stream()
                       .flatMap(GrepFlag::matchingFlags)
                       .collect(toEnumSet());
    }

    private static Stream<GrepFlag> matchingFlags(String flagString) {
        return Arrays.stream(values())
                       .filter(grepFlag -> grepFlag.code.equals(flagString));
    }

    private static Collector<GrepFlag, ?, EnumSet<GrepFlag>> toEnumSet() {
        return Collectors.toCollection(() -> EnumSet.noneOf(GrepFlag.class));
    }
}