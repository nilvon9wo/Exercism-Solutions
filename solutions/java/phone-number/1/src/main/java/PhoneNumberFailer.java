import java.util.regex.Pattern;

public class PhoneNumberFailer {
    // Error messages (exact text expected by tests)
    private static final String MESSAGE_FEWER_THAN_TEN = "must not be fewer than 10 digits";
    private static final String MESSAGE_GREATER_THAN_ELEVEN = "must not be greater than 11 digits";
    private static final String MESSAGE_ELEVEN_MUST_START_WITH_ONE = "11 digits must start with 1";

    // Magic-number replacements
    private static final int AREA_CODE_START_INDEX = 0;
    private static final int MIN_DIGITS = 10;
    private static final int MAX_DIGITS = 11;
    private static final char COUNTRY_CODE = '1';

    public void failIfMatches(String numberString, Pattern pattern, String message) {
        boolean containsMatch = pattern.matcher(numberString)
                                        .find();
        this.failIf(containsMatch, message);
    }

    public void failIfTooShort(final String digits) {
        boolean isTooShort = digits.length() < MIN_DIGITS;
        this.failIf(isTooShort, MESSAGE_FEWER_THAN_TEN);
    }

    public void failIfTooLong(final String digits) {
        boolean isTooLong = digits.length() > MAX_DIGITS;
        this.failIf(isTooLong, MESSAGE_GREATER_THAN_ELEVEN);
    }

    public void failIfInvalidCountryCode(final String digits) {
        boolean isCountryCodeInvalid =
                digits.charAt(AREA_CODE_START_INDEX) != COUNTRY_CODE;

        this.failIf(isCountryCodeInvalid, MESSAGE_ELEVEN_MUST_START_WITH_ONE);
    }

    private void failIf(final boolean condition, final String message) {
        if (condition) {
            this.fail(message);
        }
    }

    public void fail(final String message) {
        throw new IllegalArgumentException(message);
    }
}
