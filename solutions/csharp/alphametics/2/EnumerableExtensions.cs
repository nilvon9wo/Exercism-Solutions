using System;
using System.Collections.Generic;
using System.Linq;

public static class EnumerableExtensions
{
	public static HashSet<int> ExtractWordInitials(this IEnumerable<IEnumerable<int>> equation) =>
		equation.Select(word => word.First())
			.ToHashSet();

	public static List<(int target, List<(int key, int count)>)> Parse(this IEnumerable<IEnumerable<int>> input) =>
		input.Select(digits =>
			digits.Reverse()
				.Select(i => i + 1)
		) // ElementAtOrDefault default is 0 
		.Reverse()
		.Transpose()
		.Select(r =>
			r.Where(i => i > 0)
			.Select(i => i - 1)
		) // ElementAtOrDefault default is 0 
		.Select(column =>
			(
				column.First(),
				column.Skip(1)
					.GroupBy(i => i)
					.Select(group =>
						(group.Key, group.Count()))
							.ToList())
					)
		.ToList();

	// https://stackoverflow.com/questions/33336540/how-to-use-linq-to-find-all-combinations-of-count-items-from-a-set-of-numbers
	public static IEnumerable<IEnumerable<T>> ToCombinations<T>(this IEnumerable<T> values, int digits) =>
		digits == 0
			? new[] { Array.Empty<T>() }
			: values.SelectMany((e, i) =>
					values.Skip(i + 1)
						.ToCombinations(digits - 1)
						.Select(c => c.Prepend(e))
				);

	public static IEnumerable<IEnumerable<T>> Transpose<T>(this IEnumerable<IEnumerable<T>> list) =>
		!list.Any()
		? list
		: Enumerable.Range(0, list.First().Count())
			.Select(x =>
				list.Select(y => y.ElementAtOrDefault(x))
			);

	public static Dictionary<char, int> MapCharactersToIntegers(this IEnumerable<char> characters) =>
		characters.Select((character, integer) => (c: character, i: integer))
			.ToDictionary(
			   keyValuePair => keyValuePair.c,
			   keyValuePair => keyValuePair.i
		);
}
