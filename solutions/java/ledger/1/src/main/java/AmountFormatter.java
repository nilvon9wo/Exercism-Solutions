import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AmountFormatter {
    // === MAGIC NUMBERS ===
    private static final int CENTS_CONVERSION = 100;
    private static final int THOUSAND_GROUP = 3;
    private static final int DECIMAL_PLACES = 2;

    // Constants for literals
    private static final String DOT = "\\.";
    private static final String FORMAT_DECIMAL = "%." + DECIMAL_PLACES + "f";

    private static final Map<Country, CurrencyFormatterIntf> FORMATTERS =
            Map.of(
                    Country.NETHERLANDS, new NetherlandsCurrencyFormatter(),
                    Country.UNITED_STATES, new UnitedStatesCurrencyFormatter()
            );

    public String formatAmount(double change, LedgerFormat format) {
        boolean negative = change < 0;
        String amount = this.formatAbsoluteAmount(change, format);
        return FORMATTERS.get(format.country())
                       .format(format, negative, amount);
    }

    private String formatAbsoluteAmount(double change, LedgerFormat format) {
        double absoluteValue = Math.abs(change) / CENTS_CONVERSION;
        String[] parts = String.format(FORMAT_DECIMAL, absoluteValue)
                                 .split(DOT);
        Country country = format.country();
        String integerPart = this.addThousandsSeparator(parts[0], country.getThousandsSeparator());
        return integerPart + country.getDecimalSeparator() + parts[1];
    }

    private String addThousandsSeparator(String digits, String separator) {
        int length = digits.length();
        List<String> groups = IntStream.iterate(length, i -> i > 0, i -> i - THOUSAND_GROUP)
                                      .mapToObj(index -> this.getThousandsGroup(digits, index))
                                      .collect(Collectors.toList());
        Collections.reverse(groups);
        return String.join(separator, groups);
    }

    private String getThousandsGroup(String digits, int index) {
        int start = Math.max(0, index - THOUSAND_GROUP);
        return digits.substring(start, index);
    }
}
