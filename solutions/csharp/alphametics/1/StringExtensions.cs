using System.Collections.Generic;
using System.Linq;

public static class StringExtensions
{
	public static List<int> ToIntList(this string value) =>
		value.Select(x => int.Parse(x.ToString()))
			.ToList();
}
