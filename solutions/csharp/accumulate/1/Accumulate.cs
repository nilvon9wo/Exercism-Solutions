using System;
using System.Collections.Generic;

public static class AccumulateExtensions
{
    public static IEnumerable<U> Accumulate<T, U>(this IEnumerable<T> collection, Func<T, U> func)
    {
        IEnumerator<T> enumerator = collection.GetEnumerator();
        while (enumerator.MoveNext())
        {
            yield return func(enumerator.Current);
        }
    }
}