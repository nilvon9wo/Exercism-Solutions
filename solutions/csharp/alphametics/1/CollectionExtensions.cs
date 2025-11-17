using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;

public static class CollectionExtensions
{
	public static T Pop<T>(this List<T> values)
	{
		T item = values.LastOrDefault();
		values.RemoveAt(values.Count - 1);
		return item;
	}

	public static T Shift<T>(this List<T> values, bool canReturnZero = true)
		where T : INumber<T>
	{
		T item = values.FirstOrDefault();
		int i = 0;
		if (!canReturnZero && item == T.Zero)
		{
			do
			{
				i++;
				item = values[i];
			}
			while (i < values.Count && item == T.Zero);
		}

		values.RemoveAt(i);
		return item;
	}

	public static T JoinAsNumber<T>(this List<T> list)
		where T : IParsable<T>
	{
		List<string> values = list.Select(x => $"{x}")
			.ToList();
		string value = string.Join("", values);
		return T.Parse(value, CultureInfo.InvariantCulture);
	}

	public static U AddAll<T, U>(this IEnumerable<T> values, Func<T, U> func)
		where U : INumber<U> =>
			values.Aggregate(
				U.Zero,
				(seed, word) => seed + func(word)
			);
}
