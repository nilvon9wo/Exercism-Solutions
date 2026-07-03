import java.util.Arrays;
import java.util.List;

class RomanNumerals {
    private final int number;

    RomanNumerals(int number) {
        this.number = number;
    }

    private static final List<RomanSymbol> ROMAN_SYMBOLS = Arrays.asList(
            new RomanSymbol("M", 1000),
            new RomanSymbol("CM", 900),
            new RomanSymbol("D", 500),
            new RomanSymbol("CD", 400),
            new RomanSymbol("C", 100),
            new RomanSymbol("XC", 90),
            new RomanSymbol("L", 50),
            new RomanSymbol("XL", 40),
            new RomanSymbol("X", 10),
            new RomanSymbol("IX", 9),
            new RomanSymbol("V", 5),
            new RomanSymbol("IV", 4),
            new RomanSymbol("I", 1)
    );

    String getRomanNumeral() {
        int remaining = number;
        StringBuilder result = new StringBuilder();

        for (RomanSymbol romanSymbol : ROMAN_SYMBOLS) {
            while (remaining >= romanSymbol.value()) {
                result.append(romanSymbol.symbol());
                remaining -= romanSymbol.value();
            }
        }

        return result.toString();
    }
}