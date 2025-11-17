using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

[SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "Test requirement.")]
// ReSharper disable once CheckNamespace
public static class ListOps
{
	public static int Length<T>(List<T> input)
	{
		_ = input ?? throw new ArgumentNullException(nameof(input));
		return input.Count;
	}

	public static List<T> Reverse<T>(List<T> input)
	{
		_ = input ?? throw new ArgumentNullException(nameof(input));
		return new List<T>(
			input.AsEnumerable()
				.Reverse()
		);
	}

	public static List<TOut> Map<TIn, TOut>(List<TIn> input, Func<TIn, TOut> map)
	{
		_ = input ?? throw new ArgumentNullException(nameof(input));
		return input.ConvertAll(x => map(x));
	}

	public static List<T> Filter<T>(List<T> input, Func<T, bool> predicate)
		=> input.Where(predicate)
			.ToList();

	public static TOut Foldl<TIn, TOut>(List<TIn> input, TOut start, Func<TOut, TIn, TOut> func)
		=> input.Aggregate(start, func);

	public static TOut Foldr<TIn, TOut>(List<TIn> input, TOut start, Func<TIn, TOut, TOut> func)
		=> input.AsEnumerable()
			.Reverse()
			.Aggregate(start, (acc, item) => func(item, acc));

	public static List<T> Concat<T>(List<List<T>> input)
		=> input.SelectMany(list => list)
			.ToList();

	public static List<T> Append<T>(List<T> left, List<T> right)
		=> left.Concat(right)
			.ToList();
}