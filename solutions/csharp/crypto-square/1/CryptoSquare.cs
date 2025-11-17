using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

public static class CryptoSquare
{
	private static readonly Regex AlphaNumericPattern = new("[^a-zA-Z0-9]", RegexOptions.Compiled);

	public static string NormalizedPlaintext(this string plaintext) =>
		AlphaNumericPattern.Replace(plaintext, "")
			.ToLowerInvariant();

	public static IEnumerable<string> PlaintextSegments(string plaintext) =>
		throw new NotImplementedException("You need to implement this function.");

	public static string ToEncoded(this string text)
	{
		Dictionary<int, List<char>> columnCharactersByRow = CreateSquare(text);

		string[] rows = CreateRows(columnCharactersByRow);
		return string.Join(" ", rows);
	}

	private static Dictionary<int, List<char>> CreateSquare(string text)
	{
		int rowCount = CountRequiredRows(text);
		int row = 0;
		return text.Aggregate(new Dictionary<int, List<char>>(), (seed, character) =>
		{
			if (!seed.TryGetValue(row, out List<char> columnCharacters))
			{
				columnCharacters = new();
				seed[row] = columnCharacters;
			}

			columnCharacters.Add(character);
			row++;
			if (row > rowCount)
			{
				row = 0;
			}

			return seed;
		});
	}

	private static string[] CreateRows(Dictionary<int, List<char>> columnCharactersByRow)
	{
		string[] rows = columnCharactersByRow.Select(x => new string(x.Value.ToArray()))
					.ToArray();
		int rowLength = rows[0].Length;
		return rows.Select(x => x.PadRight(rowLength))
			.ToArray();
	}

	private static int CountRequiredRows(string text)
	{
		int length = text.Length;
		int squareroot = (int)Math.Sqrt(length);
		return (Math.Pow(squareroot, 2) == length)
			? squareroot - 1
			: squareroot;
	}

	public static string Ciphertext(string plaintext)
	{
		string normalizedText = plaintext.NormalizedPlaintext();
		return string.IsNullOrEmpty(normalizedText)
			? ""
			: normalizedText.ToEncoded();
	}
}
