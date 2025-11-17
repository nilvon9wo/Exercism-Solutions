using System.Collections.Generic;
using System.Linq;
using System.Text;

public static class Raindrops
{
	private static readonly Dictionary<int, string> _textByValue = new()
	{
		{ 3, "Pling" },
		{ 5, "Plang" },
		{ 7, "Plong" },
	};

	public static string Convert(int number)
	{
		string sound = _textByValue
			.Aggregate(
				  new StringBuilder(),
				  (stringBuilder, keyValuePair) =>
					{
						(int value, string text) = keyValuePair;
						return (number % value == 0)
							? stringBuilder.Append(text)
							: stringBuilder;
					})
			.ToString();

		return string.IsNullOrEmpty(sound)
			? number.ToString()
			: sound;
	}
}