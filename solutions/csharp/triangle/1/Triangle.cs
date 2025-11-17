using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

public class Triangle
{
	public IList<double> Sides { get; init; }

	public Triangle(double side1, double side2, double side3) =>
		Sides = new List<double> { side1, side2, side3 };

	public static bool IsScalene(double side1, double side2, double side3)
	{
		Triangle triangle = new(side1, side2, side3);
		return triangle.IsValidTriangle()
			&& triangle.Sides.NoEqual();
	}

	public static bool IsIsosceles(double side1, double side2, double side3)
	{
		Triangle triangle = new(side1, side2, side3);
		return triangle.IsValidTriangle()
			&& triangle.Sides.AtLeastTwoEqualValues();
	}

	public static bool IsEquilateral(double side1, double side2, double side3)
	{
		Triangle triangle = new(side1, side2, side3);
		return triangle.IsValidTriangle()
			&& triangle.Sides.AllEqual();
	}

	private bool IsValidTriangle() =>
		!Sides.AnyZero()
			&& Sides.EachValueExceedsSumOfAllOthers();
}

public static class EnumerableExtensions
{
	public static bool AnyZero<T>(this IEnumerable<T> values)
		where T : INumber<T> =>
		values is null
			? throw new ArgumentNullException(nameof(values))
			: values.Any(x => x == T.Zero);

	public static bool AllEqual<T>(this IEnumerable<T> values)
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		T first = values.FirstOrDefault();
		return values.All(x => x.Equals(first));
	}

	public static bool AtLeastTwoEqualValues<T>(this IEnumerable<T> values) => values is null
			? throw new ArgumentNullException(nameof(values))
			: values.ToCountDictionary(x => x)
					.Values.ToHashSet()
					.Any(x => x >= 2);

	public static bool NoEqual<T>(this IEnumerable<T> values)
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		HashSet<T> uniqueValues = values.ToHashSet();
		List<T> allValues = values.ToList();
		return allValues.Count == uniqueValues.Count;
	}

	public static bool EachValueExceedsSumOfAllOthers<T>(this IEnumerable<T> values)
		where T : INumber<T>
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		T sum = values.Sum();
		return values.All(x => (sum - x) > x);
	}

	public static Dictionary<TKey, int> ToCountDictionary<TCountable, TKey>(this IEnumerable<TCountable> records, Func<TCountable, TKey> keySelector) =>
		records
		.GroupBy(keySelector)
		.Select(value => new
		{
			Value = value.Key,
			Count = value.Count()
		})
		.ToDictionary(
			group => group.Value,
			group => group.Count
		);
}

public static class NumberExtensions
{
	public static T Sum<T>(this IEnumerable<T> values)
		where T : INumber<T> =>
			values.Aggregate(T.Zero, (current, value) => current + value);
}
