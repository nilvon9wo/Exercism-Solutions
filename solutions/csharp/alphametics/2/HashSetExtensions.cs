using System.Collections.Generic;
using System.Linq;

public static class HashSetExtensions
{
	public static List<bool> BuildZeroMask(this HashSet<int> noZeroSet, int size) =>
		Enumerable.Range(0, size)
			.Select(character => !noZeroSet.Contains(character))
			.ToList();
}
