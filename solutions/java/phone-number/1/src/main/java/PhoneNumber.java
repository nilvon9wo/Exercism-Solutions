import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record PhoneNumber(String cleanNumber) {
    public PhoneNumber(String cleanNumber) {
        this.cleanNumber = this.validateAndCleanNumber(cleanNumber, new PhoneNumberFailer());
    }

    // Patterns
    private static final Pattern LETTERS = Pattern.compile("[A-Za-z]");
    private static final Pattern DISALLOWED_PUNCTUATION = Pattern.compile("[@:!]");

    // Error messages (exact text expected by tests)
    private static final String MESSAGE_LETTERS = "letters not permitted";
    private static final String MESSAGE_PUNCTUATION = "punctuations not permitted";
    private static final String MESSAGE_CANNOT_START_WITH_ZERO = " cannot start with zero";
    private static final String MESSAGE_CANNOT_START_WITH_ONE = " cannot start with one";

    // Magic-number replacements
    private static final int AREA_CODE_START_INDEX = 0;
    private static final int EXCHANGE_CODE_START_INDEX = 3;
    private static final int MAX_DIGITS = 11;
    private static final char DISALLOWED_ZERO = '0';
    private static final char DISALLOWED_ONE = '1';

    public String getNumber() {
        return this.cleanNumber;
    }

    public String validateAndCleanNumber(String numberString, PhoneNumberFailer phoneNumberFailer) {
        phoneNumberFailer.failIfMatches(numberString, LETTERS, MESSAGE_LETTERS);
        phoneNumberFailer.failIfMatches(numberString, DISALLOWED_PUNCTUATION, MESSAGE_PUNCTUATION);

        String rawDigits = this.extractDigits(numberString);
        return this.getCleanNumber(rawDigits, phoneNumberFailer);
    }

    private String getCleanNumber(String rawDigits, PhoneNumberFailer phoneNumberFailer) {
        String cleanDigits = this.normalizeDigits(rawDigits, phoneNumberFailer);
        this.validateCode(cleanDigits, AREA_CODE_START_INDEX, "area code", phoneNumberFailer);
        this.validateCode(cleanDigits, EXCHANGE_CODE_START_INDEX, "exchange code", phoneNumberFailer);
        return cleanDigits;
    }

    private String extractDigits(final String input) {
        return input.chars()
                       .mapToObj(character -> String.valueOf((char) character))
                       .filter(string -> Character.isDigit(string.charAt(0)))
                       .collect(Collectors.joining());
    }

    private String normalizeDigits(final String digits, PhoneNumberFailer phoneNumberFailer) {
        phoneNumberFailer.failIfTooShort(digits);
        phoneNumberFailer.failIfTooLong(digits);

        if (digits.length() == MAX_DIGITS) {
            phoneNumberFailer.failIfInvalidCountryCode(digits);
            return this.stripLeadingCountryCode(digits);
        }

        return digits;
    }

    private String stripLeadingCountryCode(final String digits) {
        return digits.substring(1);
    }

    private void validateCode(
            final String digits,
            final int index,
            final String label,
            PhoneNumberFailer phoneNumberFailer
    ) {
        char firstChar = digits.charAt(index);
        if (firstChar == DISALLOWED_ZERO) {
            phoneNumberFailer.fail(label + MESSAGE_CANNOT_START_WITH_ZERO);
        }

        if (firstChar == DISALLOWED_ONE) {
            phoneNumberFailer.fail(label + MESSAGE_CANNOT_START_WITH_ONE);
        }
    }
}
