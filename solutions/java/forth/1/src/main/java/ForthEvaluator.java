import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ForthEvaluator {
    private static final Pattern NEW_DEFINITION_PATTERN = Pattern.compile("^:(.*);$");

    private static final Map<String, Function<Context, Context>> DEFAULT_HANDLER_BY_TOKEN
            = createDefaultHandlers();

    List<Integer> evaluateProgram(List<String> input) {
        return input.stream()
                    .reduce(
                            new Context(),
                            this::evaluate,
                            (context, instruction) -> context
                    )
                    .getStack();
    }

    private Context evaluate(Context context, String instruction) {
        Matcher instructionMatch = NEW_DEFINITION_PATTERN.matcher(instruction);
        return instructionMatch.matches()
               ? context.defineHandler(instructionMatch.group(1))
               : followInstruction(context, instruction);
    }

    private Context followInstruction(Context context, String instruction) {
        List<String> tokens = Arrays.stream(instruction.split(" "))
                                    .collect(Collectors.toCollection(java.util.ArrayList::new));
        while (!tokens.isEmpty()) {
            String nextOperation = shiftTokensToStack(context, tokens);
            context = doOperation(context, nextOperation);
        }

        return context;
    }

    private String shiftTokensToStack(Context context, List<String> tokens) {
        String nextOperation = null;
        do {
            String token = ListUtilities.shift(tokens);
            try {
                int value = Integer.parseInt(token);
                context.getStack()
                       .add(value);
            }
            catch (NumberFormatException exception) {
                nextOperation = token;
            }

        }
        while (!tokens.isEmpty() &&
                 (nextOperation == null || nextOperation.isEmpty()));

        return nextOperation;
    }

    private Context doOperation(Context context, String operation) {
        if (operation == null || operation.isEmpty()) {
            return context;
        }

        String normalizedOperation = operation.toLowerCase(Locale.ROOT);
        if (context.getInstructionsByWord()
                   .containsKey(normalizedOperation)
        ) {
            return followInstruction(
                    context,
                    context.getInstructionsByWord().get(normalizedOperation)
            );
        }

        Function<Context, Context> handler = DEFAULT_HANDLER_BY_TOKEN.get(normalizedOperation);
        if (handler != null) {
            return handler.apply(context);
        }

        throw new IllegalArgumentException(
                "No definition available for operator \"" + operation + "\""
        );
    }

    private static Map<String, Function<Context, Context>> createDefaultHandlers() {
        Map<String, Function<Context, Context>> handlers = new HashMap<>();
        handlers.put("+", MathHelpers::add);
        handlers.put("-", MathHelpers::subtract);
        handlers.put("*", MathHelpers::multiply);
        handlers.put("/", MathHelpers::divide);
        handlers.put("dup", StackHelpers::duplicateLast);
        handlers.put("drop", StackHelpers::dropLast);
        handlers.put("swap", StackHelpers::swapLast);
        handlers.put("over", StackHelpers::penultimateValueCopy);
        return handlers;
    }
}