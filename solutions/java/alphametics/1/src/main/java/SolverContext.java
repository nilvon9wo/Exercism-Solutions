import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record SolverContext(
        Puzzle puzzle,
        Map<Character, Integer> valueByLetters,
        Set<Integer> usedDigits
) {
    public SolverContext(Puzzle puzzle) {
        this(puzzle, new HashMap<>(), new HashSet<>());
    }

    public void applyDigitAssignment(char letter, int digit) {
        this.valueByLetters()
                .put(letter, digit);
        this.usedDigits()
                .add(digit);
    }

    public void revertDigitAssignment(char letter, int digit) {
        this.valueByLetters()
                .remove(letter);
        this.usedDigits()
                .remove(digit);
    }
}