final class KeyParser {
    public static Result<String> parse(Input input) {
        Result<String> keyResult = input.takeUntil(Symbol.OPTION_START.value(), "Key");
        if (!keyResult.isValid()) {
            return Result.failure(keyResult.getException());
        }

        String key = keyResult.getValue();
        if (containsLowerCase(key)) {
            return Result.failure(new IllegalArgumentException( "Key " + key + " contains disallowed lowercase."));
        }

        return keyResult;
    }

    private static boolean containsLowerCase(String value) {
        return value.chars()
                    .anyMatch(Character::isLowerCase);
    }
}