public static class StackHelpers
{
    public static Context DuplicateLast(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return context.WithStack(
            stack =>
            {
                int last = stack.RequireAtLeastOneValue()
                    .LastOrDefault();
                stack.Add(last);
            }
        );
    }

    public static Context DropLast(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return context.WithStack(
            stack =>
                _ = stack.RequireAtLeastOneValue()
                    .Pop()
        );
    }

    public static Context SwapLast(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return context.WithStack(
            stack =>
            {
                int lastValue = stack.RequireAtLeastTwoValues()
                    .Pop();
                int penultimateValue = stack.Pop();
                stack.Add(lastValue);
                stack.Add(penultimateValue);
            }
        );
    }

    public static Context PenultimateValueCopy(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return context.WithStack(
            stack =>
            {
                int penultimateValue = stack.RequireAtLeastTwoValues()[^2];
                stack.Add(penultimateValue);
            }
        );
    }
}

