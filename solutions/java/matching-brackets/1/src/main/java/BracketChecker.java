import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record BracketChecker(String expression) {

    private static final Map<Character, Character> CLOSINGS_BY_OPENINGS = Map.of(
            '(', ')',
            '[', ']',
            '{', '}'
    );

    private static final Set<Character> OPENINGS = CLOSINGS_BY_OPENINGS.keySet();
    private static final Set<Character> CLOSINGS = Set.copyOf(CLOSINGS_BY_OPENINGS.values());
    private static final Set<Character> ALL_BRACKETS = Stream.concat(OPENINGS.stream(), CLOSINGS.stream())
                                                               .collect(Collectors.toUnmodifiableSet());

    private static final boolean VALID = true;
    private static final boolean INVALID = false;

    public boolean areBracketsMatchedAndNestedCorrectly() {
        final Deque<Character> stack = new ArrayDeque<>();
        boolean allBracketsValid = expression.chars()
                            .mapToObj(character -> (char) character)
                            .filter(ALL_BRACKETS::contains)
                            .allMatch(character -> this.validateBracket(stack, character));
        return allBracketsValid && stack.isEmpty();
    }

    private boolean validateBracket(final Deque<Character> stack, final char character) {
        if (OPENINGS.contains(character)) {
            stack.addLast(character);
            return VALID;
        }
        else {
            boolean isMismatch = stack.isEmpty()
                                  || CLOSINGS_BY_OPENINGS.get(stack.getLast()) != character;
            if (isMismatch) {
                return INVALID;
            }
            else {
                stack.removeLast();
                return VALID;
            }
        }
    }
}
