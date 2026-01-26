public static class ListExtensions
{
    [SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "<Pending>")]
    public static List<T> RequireAtLeastOneValue<T>(this List<T> values)
    {
        _ = values ?? throw new ArgumentNullException(nameof(values));
        return values.RequiresCount(count => count < 1, "Operation requires at least one value.");
    }

    [SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "<Pending>")]
    public static List<T> RequireAtLeastTwoValues<T>(this List<T> values)
    {
        _ = values ?? throw new ArgumentNullException(nameof(values));
        return values.RequiresCount(count => count < 2, "Operation requires at least two values.");
    }

    [SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "<Pending>")]
    public static List<T> RequireExactlyTwoValues<T>(this List<T> values)
    {
        _ = values ?? throw new ArgumentNullException(nameof(values));
        return values.RequiresCount(count => count != 2, "Operation requires exactly two values.");
    }

    private static List<T> RequiresCount<T>(this List<T> values, Func<int, bool> validation, string errorMessage)
        => validation(values.Count)
            ? throw new InvalidOperationException(errorMessage)
            : values;

    public static T Shift<T>(this List<T> values)
    {
        _ = values ?? throw new ArgumentNullException(nameof(values));
        return TakeOne(values, x => x.FirstOrDefault());
    }

    public static T Pop<T>(this List<T> values)
    {
        _ = values ?? throw new ArgumentNullException(nameof(values));
        return TakeOne(values, x => x.LastOrDefault());
    }

    private static T TakeOne<T>(this List<T> values, Func<List<T>, T> function)
    {
        T item = function(values);
        if (item != null)
        {
            _ = values.Remove(item);
        }

        return item;
    }
}