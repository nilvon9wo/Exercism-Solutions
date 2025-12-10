import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record PhoneNumber(String cleanNumber) {
    public PhoneNumber(String cleanNumber) {
        this.cleanNumber = this.validateAndCleanNumber(cleanNumber);
    }

    private static final Pattern LETTERS = Pattern.compile("[A-Za-z]");
    private static final Pattern DISALLOWED_PUNCTUATION = Pattern.compile("[@:!]");

    private static final String MESSAGE_LETTERS = "letters not permitted";
    private static final String MESSAGE_PUNCTUATION = "punctuations not permitted";
    private static final String MESSAGE_CANNOT_START_WITH_ZERO = " cannot start with zero";
    private static final String MESSAGE_CANNOT_START_WITH_ONE = " cannot start with one";
    private static final String MESSAGE_FEWER_THAN_TEN = "must not be fewer than 10 digits";
    private static final String MESSAGE_GREATER_THAN_ELEVEN = "must not be greater than 11 digits";
    private static final String MESSAGE_ELEVEN_MUST_START_WITH_ONE = "11 digits must start with 1";

    private static final int AREA_CODE_START_INDEX = 0;
    private static final int EXCHANGE_CODE_START_INDEX = 3;
    private static final int MAX_DIGITS = 11;
    private static final int MIN_DIGITS = 10;
    private static final char US_COUNTRY_CODE = '1';


    public String getNumber() {
        return this.cleanNumber;
    }

    public String validateAndCleanNumber(String numberString) {
        this.failIfMatches(numberString, LETTERS, MESSAGE_LETTERS);
        this.failIfMatches(numberString, DISALLOWED_PUNCTUATION, MESSAGE_PUNCTUATION);

        String rawDigits = this.extractDigits(numberString);
        return this.getCleanNumber(rawDigits);
    }

    private String getCleanNumber(String rawDigits) {
        String cleanDigits = this.normalizeDigits(rawDigits);
        this.validateCode(cleanDigits, AREA_CODE_START_INDEX, "area code");
        this.validateCode(cleanDigits, EXCHANGE_CODE_START_INDEX, "exchange code");
        return cleanDigits;
    }

    private String extractDigits(final String input) {
        return input.chars()
                       .mapToObj(character -> String.valueOf((char) character))
                       .filter(string -> Character.isDigit(string.charAt(0)))
                       .collect(Collectors.joining());
    }

    private String normalizeDigits(final String digits) {
        this.failIf(digits.length() < MIN_DIGITS, MESSAGE_FEWER_THAN_TEN);
        this.failIf(digits.length() > MAX_DIGITS, MESSAGE_GREATER_THAN_ELEVEN);

        if (digits.length() == MAX_DIGITS) {
            boolean isNotAmerican = digits.charAt(AREA_CODE_START_INDEX) != US_COUNTRY_CODE;
            this.failIf(isNotAmerican, MESSAGE_ELEVEN_MUST_START_WITH_ONE);
            return this.stripLeadingCountryCode(digits);
        }

        return digits;
    }

    private String stripLeadingCountryCode(final String digits) {
        return digits.substring(1);
    }

    private void validateCode(final String digits, final int index, final String label) {
        char firstChar = digits.charAt(index);
        this.failIf(firstChar == '0',label + MESSAGE_CANNOT_START_WITH_ZERO);
        this.failIf(firstChar == '1', label + MESSAGE_CANNOT_START_WITH_ONE);
    }

    private void failIfMatches(String numberString, Pattern pattern, String message) {
        boolean containsMatch = pattern.matcher(numberString)
                                        .find();
        this.failIf(containsMatch, message);
    }

    private void failIf(final boolean condition, final String message) {
        if (condition) {
            this.fail(message);
        }
    }

    private void fail(final String message) {
        throw new IllegalArgumentException(message);
    }
}
