using System;
using System.Collections.Generic;
using System.Linq;

// Derived from Martin Freedman's solution, but refactored to fit my coding preferences
// https://exercism.org/tracks/csharp/exercises/alphametics/solutions/martinfreedman

public class Alphametics
{
	private static readonly List<int> SingleDigitNumbers = Enumerable.Range(0, 10)
		.ToList();

	public static IDictionary<char, int> Solve(string equation)
	{
		IEnumerable<char> activeLetters = ExtractUnknowns(equation);
		int activeLetterCount = activeLetters.Count();
		List<List<int>> tokens = activeLetters.MapCharactersToIntegers()
			.Tokenise(equation);

		List<(int target, List<(int key, int count)>)> columns = tokens.Parse();
		List<bool> zeroMask = tokens.ExtractWordInitials()
			.BuildZeroMask(activeLetterCount);

		List<int> result = SingleDigitNumbers.CreateCombinations(activeLetterCount)
			.Where(tokenCharacters => CanBeZero(tokenCharacters, zeroMask))
			.Where(permutation => ColumnSum(columns, 0, permutation))
			.FirstOrDefault();

		return result != null
			? ToResultDictionary(activeLetters, result)
			: throw new ArgumentException(nameof(equation));
	}

	private static IEnumerable<char> ExtractUnknowns(string equation) =>
		equation.Where(c => !" +=".Contains(c))
			.Distinct();

	private static bool CanBeZero(List<int> tokens, List<bool> zeroMask)
	{
		int found = tokens.FindIndex(i => i == 0);
		return found == -1 || zeroMask[found];
	}

	private static bool ColumnSum(List<(int y, List<(int key, int count)> xs)> column, int carry, List<int> permutation)
	{
		List<(int y, List<(int key, int count)> xs)>.Enumerator enumerator = column.GetEnumerator();
		while (enumerator.MoveNext())
		{
			(int y, List<(int key, int count)> xs) = (enumerator.Current.y, enumerator.Current.xs);
			int sum = xs.Sum(k => k.count * permutation[k.key]) + carry;
			if (permutation[y] == sum % 10)
			{
				carry = sum / 10;
			}
			else
			{
				return false;
			}
		}

		return carry == 0;
	}

	private static Dictionary<char, int> ToResultDictionary(IEnumerable<char> activeLetters, List<int> result) =>
		result.Zip(
				activeLetters,
				(digit, letter) => (c: letter, i: digit)
		)
		.ToDictionary(
			keyValuePair => keyValuePair.c,
			keyValuePair => keyValuePair.i
		);
}