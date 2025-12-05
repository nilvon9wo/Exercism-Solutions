record Atbash(
        AtbashLetterInverter letterInverter,
        AtbashTextGrouper atbashTextGrouper,
        TextSanitizer textSanitizer
) {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int DEFAULT_GROUP_SIZE = 5;

    public Atbash() {
        this(
                new AtbashLetterInverter(ALPHABET),
                new AtbashTextGrouper(DEFAULT_GROUP_SIZE),
                new TextSanitizer()
        );
    }

    String encode(String input) {
        String preprocessedText = preprocessText(input);
        return atbashTextGrouper.groupText(preprocessedText);
    }

    String decode(String input) {
        return preprocessText(input);
    }

    private String preprocessText(String input) {
        String sanitizedText = this.textSanitizer.keepLettersAndDigitsLowercase(input);
        return this.letterInverter.invertLetters(sanitizedText);
    }
}
