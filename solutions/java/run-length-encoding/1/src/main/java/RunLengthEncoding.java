import java.util.function.BiFunction;
import java.util.stream.IntStream;

public final class RunLengthEncoding {
    public String encode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char firstCharacter = input.charAt(0);
        RunLengthState initialState = new RunLengthState("", firstCharacter, 1);
        IntStream remainingChars = input.chars()
                                           .skip(1);
        RunLengthState finalState = this.reduceChars(remainingChars, initialState, RunLengthState::addCharacter);
        return this.appendFinalCharacterWithCount(finalState);
    }

    private String appendFinalCharacterWithCount(RunLengthState finalState) {
        Object countSuffix = finalState.count() > 1
                                     ? finalState.count()
                                     : "";
        return finalState.result() + countSuffix + finalState.character();
    }

    public String decode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        RunLengthState initialState = new RunLengthState("", '\0', 0);
        return reduceChars(input.chars(), initialState, this.decodeStep())
                       .result();
    }

    private RunLengthState reduceChars(
            IntStream input,
            RunLengthState initialState,
            BiFunction<RunLengthState, Character, RunLengthState> stepFunction
    ) {
        return input
                       .mapToObj(c -> (char) c)
                       .reduce(initialState, stepFunction, (state1, state2) -> state2);
    }

    private BiFunction<RunLengthState, Character, RunLengthState> decodeStep() {
        return (state, character) -> Character.isDigit(character)
                                             ? state.addDigit(character)
                                             : state.addDecodedCharacter(character);
    }
}
