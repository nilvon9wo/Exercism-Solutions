using System.Globalization;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Luhn
{
	private static readonly CultureInfo _invariantCulture = CultureInfo.InvariantCulture;

	public static bool IsValid(string number)
	{
		if (number.Any(InvalidCharacter))
		{
			return false;
		}

		string cleanNumber = Clean(number);
		return (cleanNumber.Length > 1) && IsLuhnValid(cleanNumber);
	}

	private static bool InvalidCharacter(char character)
		=> !char.IsDigit(character) &&
		   (character != ' ');

	private static string Clean(string number)
		=> new(
			number.Where(char.IsDigit)
				.ToArray()
		);

	private static bool IsLuhnValid(string number)
	{
		bool isSecondDigit = false;
		int sum = number
			.Reverse()
			.Select(
				digit => CalculateLuhnValue(digit, ref isSecondDigit)
			)
			.Aggregate(0, (acc, value) => acc + value);

		return (sum % 10) == 0;
	}

	private static int CalculateLuhnValue(char digit, ref bool isSecondDigit)
	{
		string digitString = digit.ToString();
		int value = int.Parse(digitString, _invariantCulture);
		if (isSecondDigit)
		{
			value *= 2;
			if (value > 9)
			{
				value -= 9;
			}
		}

		isSecondDigit = !isSecondDigit;
		return value;
	}
}