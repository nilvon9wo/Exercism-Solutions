using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Sublist
{
	[SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "Test requirement.")]
	public static SublistType Classify<T>(List<T> list1, List<T> list2)
		where T : IComparable
	{
		_ = list1 ?? throw new ArgumentNullException(nameof(list1));
		_ = list2 ?? throw new ArgumentNullException(nameof(list2));
		return list1.SequenceEqual(list2)
			? SublistType.Equal
			: IsSuperList(list1, list2)
				? SublistType.Superlist
				: IsSuperList(list2, list1)
					? SublistType.Sublist
					: SublistType.Unequal;
	}

	private static bool IsSuperList<T>(IReadOnlyCollection<T> list1, IReadOnlyCollection<T> list2)
		where T : IComparable
		=> list2.Count == 0
		   || Enumerable.Range(0, list1.Count - list2.Count + 1)
			   .Any(index => IsEqualAt(index, list1, list2));

	private static bool IsEqualAt<T>(int index, IEnumerable<T> list1, IReadOnlyCollection<T> list2)
		where T : IComparable
		=> list1.Skip(index)
			.Take(list2.Count)
			.SequenceEqual(list2);
}

public enum SublistType
{
	Equal,
	Unequal,
	Superlist,
	Sublist
}