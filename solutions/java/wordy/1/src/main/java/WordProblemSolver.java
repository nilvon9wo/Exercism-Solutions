import java.util.*;
import java.util.regex.Pattern;

class WordProblemSolver {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Map<String, String> NORMALIZATION_MAP = Map.of(
            "multiplied by", "multiplied_by",
            "divided by", "divided_by"
    );

    int solve(final String wordProblem) {
        String expression = this.normalize(wordProblem);
        List<String> tokens = Arrays.stream(WHITESPACE_PATTERN.split(expression))
                                    .toList();
        if (tokens.isEmpty()) {
            throw this.createNonunderstandingException();
        }

        return evaluate(tokens);
    }

    private String normalize(String input) {
        if (input == null || input.isBlank()) {
            throw this.createNonunderstandingException();
        }

        String trimmed = input.trim();
        if (!trimmed.startsWith("What is ") || !trimmed.endsWith("?")) {
            throw this.createNonunderstandingException();
        }

        String core = trimmed.substring(8, trimmed.length() - 1)
                             .trim();
        return this.applyNormalization(core);
    }

    private String applyNormalization(String input) {
        String result = input;
        for (Map.Entry<String, String> entry : NORMALIZATION_MAP.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private int evaluate(List<String> tokens) {
        int result = this.parseNumber(tokens.get(0));
        for (int i = 1; i < tokens.size(); i += 2) {
            if (i + 1 >= tokens.size()) {
                throw this.createNonunderstandingException();
            }

            String operation = tokens.get(i);
            int rightHandSide = this.parseNumber(tokens.get(i + 1));
            result = this.apply(result, operation, rightHandSide);
        }

        return result;
    }

    private int parseNumber(String token) {
        try {
            return Integer.parseInt(token);
        }
        catch (NumberFormatException e) {
            throw this.createNonunderstandingException();
        }
    }

    private int apply(int leftHandSide, String operation, int rightHandSide) {
        return switch (operation) {
            case "plus" -> leftHandSide + rightHandSide;
            case "minus" -> leftHandSide - rightHandSide;
            case "multiplied_by" -> leftHandSide * rightHandSide;
            case "divided_by" -> leftHandSide / rightHandSide;
            default -> throw this.createNonunderstandingException();
        };
    }

    private IllegalArgumentException createNonunderstandingException() {
        return new IllegalArgumentException("I'm sorry, I don't understand the question!");
    }
}