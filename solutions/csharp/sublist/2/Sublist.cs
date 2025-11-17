using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Sublist
{
    public static SublistType Classify<T>(List<T> leftList, List<T> rightList)
        where T : IComparable
    {
        _ = leftList ?? throw new ArgumentNullException(nameof(leftList));
        _ = rightList ?? throw new ArgumentNullException(nameof(rightList));
        return leftList.SequenceEqual(rightList)
            ? SublistType.Equal
            : leftList.IsSuperlistOf(rightList)
                ? SublistType.Superlist
                : leftList.IsSublistOf(rightList)
                    ? SublistType.Sublist
                    : SublistType.Unequal;
    }
}

public static class ListExtensions
{
    public static bool IsSublistOf<T>(this IReadOnlyCollection<T> leftList, IReadOnlyCollection<T> rightList)
        where T : IComparable
        => rightList.IsSuperlistOf(leftList);

    public static bool IsSuperlistOf<T>(this IReadOnlyCollection<T> leftList, IReadOnlyCollection<T> rightList)
        where T : IComparable
        => leftList.Count >= rightList.Count
           && (rightList.Count == 0 || IsSublistContainedIn(leftList, rightList));

    private static bool IsSublistContainedIn<T>(IReadOnlyCollection<T> leftList, IReadOnlyCollection<T> rightList)
        where T : IComparable
        => Enumerable.Range(0, leftList.Count - rightList.Count + 1)
            .Any(index => leftList.IsEqualAt(index, rightList));

    private static bool IsEqualAt<T>(this IEnumerable<T> leftList, int index, IReadOnlyCollection<T> rightList)
        where T : IComparable
        => leftList.Skip(index)
            .Take(rightList.Count)
            .SequenceEqual(rightList);
}

public enum SublistType
{
    Equal,
    Unequal,
    Superlist,
    Sublist
}