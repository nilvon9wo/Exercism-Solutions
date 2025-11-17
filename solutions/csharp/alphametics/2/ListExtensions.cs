using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;

public static class ListExtensions
{
	public static IEnumerable<List<T>> CreateCombinations<T>(this List<T> values, int digits) =>
		digits == values.Count
			? values.IterativeHeapPermute()
			: values.ToCombinations(digits)
				.SelectMany(value =>
					value.ToList()
						.IterativeHeapPermute()
				);

	// https://en.wikipedia.org/wiki/Heap%27s_algorithm
	private static IEnumerable<List<T>> IterativeHeapPermute<T>(this List<T> list)
	{
		int count = list.Count;
		int[] integers = new int[count];
		yield return list;

		int i = 0; // error on wiki page says 1 should be 0
		while (i < count)
		{
			if (integers[i] < i)
			{
				if ((i & 1) == 0)
				{
					list.Swap(0, i);
				}
				else
				{
					list.Swap(integers[i], i);
				}

				yield return list;

				integers[i]++;
				i = 0; // error on wiki page says 1 should be 0
			}

			integers[i] = 0;
			i++;
		}
	}

	[MethodImpl(MethodImplOptions.AggressiveInlining)]
	private static void Swap<T>(this List<T> list, int from, int to) =>
		(list[from], list[to]) = (list[to], list[from]);

}
