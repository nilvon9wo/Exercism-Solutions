import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record Puzzle(
        List<String> addends,
        String result,
        List<Character> letters,
        Set<Character> leadingLetters
) {
    public Puzzle(
            List<String> addends,
            String result,
            Set<Character> letters,
            Set<Character> leadingLetters
    ) {
        this(addends, result, new ArrayList<>(letters), leadingLetters);
    }
}