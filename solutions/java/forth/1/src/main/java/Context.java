import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Context {
    private final Map<String, String> instructionsByWord = new HashMap<>();
    public Map<String, String> getInstructionsByWord() {
        return instructionsByWord;
    }

    private final List<Integer> stack = new ArrayList<>();
    public List<Integer> getStack() {
        return stack;
    }

    @Override
    public String toString() {
        return String.join(
                " ",
                stack.stream()
                     .map(String::valueOf)
                     .toList()
        );
    }

    public Context defineHandler(String definition) {
        if (definition == null) {
            throw new NullPointerException("group");
        }

        List<String> words = Stream.of(definition.split(" "))
                                   .filter(word -> !word.isEmpty())
                                   .collect(java.util.stream.Collectors.toList());
        ListUtilities.requireAtLeastTwoValues("Defining", words);
        String word = ListUtilities.shift(words);
        if (isNumber(word)) {
            throw new IllegalArgumentException("Cannot redefine numbers");
        }

        instructionsByWord.put(
                word.toLowerCase(Locale.ROOT),
                createInstruction(words)
        );
        return this;
    }

    private String createInstruction(List<String> definition) {
        StringBuilder builder = new StringBuilder();
        definition.forEach(word -> {
            String instruction = instructionsByWord.get(
                    word.toLowerCase(Locale.ROOT)
            );

            builder.append(instruction != null ? instruction : word)
                   .append(" ");
        });

        return builder.toString()
                      .trim();
    }

    public Context withStack(Consumer<List<Integer>> function) {
        if (function == null) {
            throw new NullPointerException("function");
        }

        function.accept(stack);
        return this;
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }
}