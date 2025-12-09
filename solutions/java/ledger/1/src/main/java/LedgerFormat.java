import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public record LedgerFormat(
        Country country,
        Currency currency
    ) {
    public static LedgerFormat of(String currencyCode, String locale) {
        return new LedgerFormat(
                Country.fromLocale(locale),
                Currency.fromCode(currencyCode)
        );
    }

    private static final int DATE_WIDTH = 10;
    private static final int DESC_WIDTH = 25;
    private static final int CHANGE_WIDTH = 13;

    public Stream<String> getHeaderStream() {
        String header = this.getHeader();
        return Stream.of(header);
    }

    private String getHeader() {
        String dateLabel = this.country.getDateLabel();
        String descriptionLabel = this.country.getDescriptionLabel();
        String changeLabel = this.country.getChangeLabel();
        return String.format("%-" + DATE_WIDTH + "s | %-" + DESC_WIDTH + "s | %-" + CHANGE_WIDTH + "s",
                dateLabel, descriptionLabel, changeLabel);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        String datePattern = this.getDatePattern();
        return DateTimeFormatter.ofPattern(datePattern);
    }


    private String getDatePattern() {
        return this.country()
                       .getDatePattern();
    }
}
