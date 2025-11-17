using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Say
{
	private const string _zero = "zero";

	private static readonly Dictionary<int, string> _onesToNinetyNine = new()
	{
		{ 0, _zero },
		{ 1, "one" },
		{ 2, "two" },
		{ 3, "three" },
		{ 4, "four" },
		{ 5, "five" },
		{ 6, "six" },
		{ 7, "seven" },
		{ 8, "eight" },
		{ 9, "nine" },
		{ 10, "ten" },
		{ 11, "eleven" },
		{ 12, "twelve" },
		{ 13, "thirteen" },
		{ 14, "fourteen" },
		{ 15, "fifteen" },
		{ 16, "sixteen" },
		{ 17, "seventeen" },
		{ 18, "eighteen" },
		{ 19, "nineteen" },
		{ 20, "twenty" },
		{ 30, "thirty" },
		{ 40, "forty" },
		{ 50, "fifty" },
		{ 60, "sixty" },
		{ 70, "seventy" },
		{ 80, "eighty" },
		{ 90, "ninety" },
	};

	private static readonly Dictionary<int, string> _scaleWordByChunkIndex = new()
	{
		{ 0, "" },
		{ 1, "thousand" },
		{ 2, "million" },
		{ 3, "billion" },
		{ 4, "trillion" },
	};

	public static string InEnglish(long number)
	{
		switch (number)
		{
			case < 0 or > 999999999999:
				throw new ArgumentOutOfRangeException(nameof(number));
			case 0:
				return _zero;
		}

		IEnumerable<long> chunks = SplitIntoThousands(number);
		string[] englishChunks = chunks
			.Select(ConvertChunkWithScaleToWords)
			.Reverse()
			.ToArray();

		return string.Join(" ", englishChunks)
			.Trim();
	}

	private static string ConvertChunkWithScaleToWords(long chunk, int index)
	{
		if (chunk == 0)
		{
			return "";
		}

		string scale = _scaleWordByChunkIndex.TryGetValue(index, out string? scaleWord)
			? string.IsNullOrEmpty(scaleWord)
				? ""
				: $" {scaleWord}"
			: throw new ArgumentOutOfRangeException(nameof(index));

		return $"{ConvertChunkToEnglish(chunk)}{scale}";
	}

	private static IEnumerable<long> SplitIntoThousands(long number)
	{
		if (number == 0)
		{
			yield return 0;
		}
		else
		{
			while (number > 0)
			{
				yield return number % 1000;
				number /= 1000;
			}
		}
	}

	private static string ConvertChunkToEnglish(long chunk)
		=> chunk switch
		{
			0 => _zero,
			< 100 => GetNumberFromDictionary((int)chunk),
			< 1000 => ConvertThreeDigitChunkToWords(chunk),
			_ => throw new ArgumentOutOfRangeException(nameof(chunk)),
		};

	private static string GetNumberFromDictionary(int number)
		=> number < 20
			? _onesToNinetyNine[number]
			: ConvertTwoDigitNumberToWords(number);

	private static string ConvertThreeDigitChunkToWords(long chunk)
	{
		int hundreds = (int)(chunk / 100);
		long tensOnes = chunk % 100;

		string hundredsWord = _onesToNinetyNine[hundreds];
		string tensOnesWord = ConvertChunkToEnglish(tensOnes);

		return (tensOnesWord != _zero) && !string.IsNullOrWhiteSpace(tensOnesWord)
			? $"{hundredsWord} hundred {tensOnesWord}"
			: $"{hundredsWord} hundred";
	}

	private static string ConvertTwoDigitNumberToWords(int number)
	{
		int ones = number % 10;
		int tens = number - ones;
		return _onesToNinetyNine[tens]
			   + (ones > 0
				   ? $"-{_onesToNinetyNine[ones]}"
				   : "");
	}
}