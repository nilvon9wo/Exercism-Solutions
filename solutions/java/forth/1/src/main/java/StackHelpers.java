import java.util.List;

public final class StackHelpers {
    public static Context duplicateLast(Context context) {
        requireContext(context);
        return context.withStack(stack -> {
            int last = ListUtilities.requireAtLeastOneValue("Duplicating", stack)
                                    .get(stack.size() - 1);
            stack.add(last);
        });
    }

    public static Context dropLast(Context context) {
        requireContext(context);
        return context.withStack(stack -> {
            final List<Integer> values = ListUtilities.requireAtLeastOneValue("Dropping", stack);
            ListUtilities.pop(values);
        });
    }

    public static Context swapLast(Context context) {
        requireContext(context);
        return context.withStack(stack -> {
            final List<Integer> values = ListUtilities.requireAtLeastTwoValues("Swapping", stack);
            int lastValue = ListUtilities.pop(values);
            int penultimateValue = ListUtilities.pop(stack);
            stack.add(lastValue);
            stack.add(penultimateValue);
        });
    }

    public static Context penultimateValueCopy(Context context) {
        requireContext(context);
        return context.withStack(stack -> {
            List<Integer> validatedStack = ListUtilities.requireAtLeastTwoValues("Overing", stack);
            int penultimateValue = validatedStack.get(validatedStack.size() - 2);
            stack.add(penultimateValue);
        });
    }

    private static void requireContext(Context context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
    }
}