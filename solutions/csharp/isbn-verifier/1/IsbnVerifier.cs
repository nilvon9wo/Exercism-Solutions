using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class IsbnVerifier
{
	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;
	private const int _isbnLength = 10;
	private const int _unexpectedValue = -1;
	private const int _checksumValue = 10;

	public static bool IsValid(string number)
	{
		if (string.IsNullOrWhiteSpace(number))
		{
			return false;
		}

		string cleanedNumber = CleanNumber(number);
		if (cleanedNumber.Length != _isbnLength)
		{
			return false;
		}

		int[] digits = ParseCharactersAsIntegers(cleanedNumber);
		if (digits.Contains(_unexpectedValue))
		{
			return false;
		}

		int checksum = CalculateChecksum(digits);
		return (checksum % 11) == 0;
	}

	private static int CalculateChecksum(IEnumerable<int> digits)
		=> digits.Select((digit, index) => digit * (_isbnLength - index))
			.Sum();

	private static int[] ParseCharactersAsIntegers(string cleanedNumber)
		=> cleanedNumber.Select(
				(c, index) => c == 'X'
					? index == (_isbnLength - 1)
						? _checksumValue
						: _unexpectedValue
					: int.TryParse(c.ToString(), out int digit)
						? digit
						: _unexpectedValue
			)
			.ToArray();

	private static string CleanNumber(string number)
		=> number.Replace("-", "", _invariantCulture);
}