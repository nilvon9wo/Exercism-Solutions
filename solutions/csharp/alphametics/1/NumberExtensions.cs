using System.Collections.Generic;
using System.Globalization;
using System.Linq;

public static class NumberExtensions
{
	public static List<int> ToDigitList<T>(this T value) =>
		$"{value}"
			.ToCharArray()
			.Select(x => int.Parse($"{x}", CultureInfo.InvariantCulture))
			.ToList();

	public static bool AllUniqueDigits<T>(this T value)
	{
		string valueString = value.ToString();
		HashSet<char> uniqueCharacters = valueString.ToCharArray()
			.ToHashSet();
		return valueString.Length == uniqueCharacters.Count();
	}
}
