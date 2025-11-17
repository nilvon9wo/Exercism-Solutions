using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

public class AlphameticPuzzle
{
	private const string _addendsGroup = "addends";
	private const string _sumGroup = "sum";
	private static readonly Regex PuzzlePattern = new(@$"(?<{_addendsGroup}>[a-zA-Z\+\s]+) == (?<{_sumGroup}>[a-zA-Z\s]+)", RegexOptions.Compiled);

	private readonly string[] _addendWords;

	public HashSet<char> AddendLetters { get; init; }
	public char[][] AddendMatrix { get; init; }

	private readonly string _sumWord;
	public char[] SumWord { get; init; }

	public int PlaceCount { get; init; }

	public HashSet<char> ActiveLetters { get; init; }
	public HashSet<char> NonZeroLetters { get; init; }

	public AlphameticPuzzle(string equation)
	{
		Match match = PuzzlePattern.Match(equation);
		if (!match.Success)
		{
			throw new ArgumentException("Invalid equation.", nameof(equation));
		}

		_sumWord = match.Groups[_sumGroup].Value.Trim();
		SumWord = _sumWord.Reverse()
			.ToArray();
		PlaceCount = _sumWord.Length;

		_addendWords = match.Groups[_addendsGroup].Value
						.Split("+")
						.Select(x => x.Trim())
						.ToArray();
		AddendLetters = _addendWords.SelectMany(x => x.ToCharArray())
			.ToHashSet();

		AddendMatrix = _addendWords.Select(Normalize)
			.ToArray();

		string[] allWords = _addendWords.Concat(new List<string> { _sumWord })
			.ToArray();
		ActiveLetters = allWords.SelectMany(x => x)
		   .ToHashSet();
		NonZeroLetters = allWords.Where(x => x.Length > 1)
			.Select(x => x[0])
			.ToHashSet();
	}

	private char[] Normalize(string word) =>
		word.PadLeft(PlaceCount, AlphaDictionary.Placeholder)
			.Reverse()
			.ToArray();

	public bool Test(AlphaDictionary dictionary)
	{
		UInt128 addendSum = _addendWords.AddAll(dictionary.Convert);
		UInt128 expectedSum = dictionary.Convert(_sumWord);
		return addendSum == expectedSum;
	}
}
