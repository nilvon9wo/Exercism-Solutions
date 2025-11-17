using System.Collections.Generic;
using System.Linq;
using System.Text;

public static class RomanNumeralExtension
{
	private const string Unassigned = "-";

	private static readonly Dictionary<int, string> RomanByValues = new()
	{
		{ 1, "I" },
		{ 4, "IV" },
		{ 5, "V" },
		{ 9, "IX" },
		{ 10, "X" },
		{ 40, "XL" },
		{ 50, "L" },
		{ 90, "XC" },
		{ 100, "C" },
		{ 400, "CD" },
		{ 500, "D" },
		{ 900, "CM" },
		{ 1000, "M" }
	};

	private static readonly HashSet<int> DescendingValues = RomanByValues.Keys
		.OrderByDescending(x => x)
		.ToHashSet();

	public static string ToRoman(this int value)
	{
		StringBuilder stringBuilder = new();
		foreach (int i in DescendingValues)
		{
			string currentChar = RomanByValues[i];
			while (value >= i && value - i >= 0)
			{
				stringBuilder = stringBuilder.Append(currentChar);
				value -= i;
			}
		}

		return stringBuilder.ToString();
	}
}
