public static class MathHelpers
{
    public static Context Add(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return DoMath(context, (x, y) => x + y);
    }

    public static Context Subtract(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return DoMath(context, (x, y) => x - y);
    }

    public static Context Multiply(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return DoMath(context, (x, y) => x * y);
    }

    public static Context Divide(Context context)
    {
        _ = context ?? throw new ArgumentNullException(nameof(context));
        return DoMath(context, (x, y) => x / y);
    }

    private static Context DoMath(Context context, Func<int, int, int> function)
        => context.WithStack(
            stack =>
            {
                int x = stack.RequireExactlyTwoValues()
                    .Shift();
                int y = stack.Shift();
                int result = function(x, y);
                stack.Add(result);
            }
        );
}

