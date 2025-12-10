import java.util.stream.IntStream;

public record RunLengthState(String result, char character, int count) {

    private static final int INITIAL_COUNT = 1;
    private static final int DIGIT_BASE = 10;
    private static final char CHAR_ZERO = '0';

    RunLengthState addCharacter(char nextCharacter) {
        return nextCharacter == this.character
                       ? new RunLengthState(result, this.character, this.count + INITIAL_COUNT)
                       : new RunLengthState(this.getNewResult(), nextCharacter, INITIAL_COUNT);
    }

    private String getNewResult() {
        Object toAppend = this.count > INITIAL_COUNT
                                  ? this.count
                                  : "";
        return this.result + toAppend + this.character;
    }

    RunLengthState addDecodedCharacter(char nextCharacter) {
        int repeatCount = this.count == 0
                                  ? 1
                                  : this.count;
        String repeated = IntStream.range(0, repeatCount)
                                  .mapToObj(i -> String.valueOf(nextCharacter))
                                  .reduce("", String::concat);
        String newResult = this.result + repeated;
        return new RunLengthState(newResult, nextCharacter, 0);
    }

    public RunLengthState addDigit(Character character) {
        int newCount = this.count() * DIGIT_BASE + (character - CHAR_ZERO);
        return new RunLengthState(this.result(), this.character(), newCount);
    }
}
