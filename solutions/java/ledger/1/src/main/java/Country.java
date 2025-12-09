import java.util.Map;

public enum Country {
    UNITED_STATES("en-US", "MM/dd/yyyy", ".", ",",
            "Date", "Description", "Change"),
    NETHERLANDS("nl-NL", "dd/MM/yyyy", ",", ".",
            "Datum", "Omschrijving", "Verandering");

    private final String localeCode;
    private final String datePattern;
    private final String decimalSeparator;
    private final String thousandsSeparator;

    private final String dateLabel;
    private final String descriptionLabel;
    private final String changeLabel;

    Country(String localeCode, String datePattern, String decimalSeparator, String thousandsSeparator,
            String dateLabel, String descriptionLabel, String changeLabel) {
        this.localeCode = localeCode;
        this.datePattern = datePattern;
        this.decimalSeparator = decimalSeparator;
        this.thousandsSeparator = thousandsSeparator;
        this.dateLabel = dateLabel;
        this.descriptionLabel = descriptionLabel;
        this.changeLabel = changeLabel;
    }

    public String getDatePattern() { return this.datePattern; }
    public String getDecimalSeparator() { return this.decimalSeparator; }
    public String getThousandsSeparator() { return this.thousandsSeparator; }

    public String getDateLabel() { return this.dateLabel; }
    public String getDescriptionLabel() { return this.descriptionLabel; }
    public String getChangeLabel() { return this.changeLabel; }

    private static final String ERROR_INVALID_LOCALE = "Invalid locale";

    private static final Map<String, Country> COUNTRY_BY_LOCALE_CODES = Map.of(
            UNITED_STATES.localeCode, UNITED_STATES,
            NETHERLANDS.localeCode, NETHERLANDS
    );

    public static Country fromLocale(String locale) {
        Country country = COUNTRY_BY_LOCALE_CODES.get(locale);
        if (country == null) {
            throw new IllegalArgumentException(ERROR_INVALID_LOCALE);
        }

        return country;
    }
}
