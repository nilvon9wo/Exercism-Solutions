import java.util.function.IntBinaryOperator;

public final class MathHelpers {
    public static Context add(Context context) {
        requireContext(context);
        return doMath(context, "Addition", Integer::sum);
    }

    public static Context subtract(Context context) {
        requireContext(context);
        return doMath(context, "Subtraction", (x, y) -> x - y);
    }

    public static Context multiply(Context context) {
        requireContext(context);
        return doMath(context, "Multiplication", (x, y) -> x * y);
    }

    public static Context divide(Context context) {
        requireContext(context);
        return doMath(
                context, "Division", (x, y) -> {
                    if (y == 0) {
                        throw new IllegalArgumentException("Division by 0 is not allowed");
                    }

                    return x / y;
                }
        );
    }

    private static Context doMath(
            Context context,
            String operationName,
            IntBinaryOperator operation
    ) {
        return context.withStack(stack -> {
            ListUtilities.requireAtLeastTwoValues(operationName, stack);
            int value1 = ListUtilities.pop(stack);
            int value2 = ListUtilities.pop(stack);
            stack.add(operation.applyAsInt(value2, value1));
        });
    }

    private static void requireContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
    }
}