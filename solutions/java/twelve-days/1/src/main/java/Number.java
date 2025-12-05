import java.util.Arrays;
import java.util.Locale;

enum Number {
    One(1, "first"),
    Two(2, "second"),
    Three(3, "third"),
    Four(4, "fourth"),
    Five(5, "fifth"),
    Six(6, "sixth"),
    Seven(7, "seventh"),
    Eight(8, "eighth"),
    Nine(9, "ninth"),
    Ten(10, "tenth"),
    Eleven(11, "eleventh"),
    Twelve(12, "twelfth");

    private final int value;
    private final String ordinal;

    Number(int value, String ordinal) {
        this.value = value;
        this.ordinal = ordinal;
    }

    static Number fromInt(int value) {
        return Arrays.stream(values())
                       .filter(n -> n.value == value)
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("Invalid number: " + value));
    }

    String toOrdinal() {
        return this.ordinal;
    }

    String toLowercaseString() {
        return name()
                    .toLowerCase(Locale.ROOT);
    }
}