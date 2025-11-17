#nullable enable

using System;
using System.Collections.Generic;
using System.Linq;

public static class Alphametics
{
	private const int InvalidCarry = -1;

	public static IDictionary<char, int> Solve(string equation)
	{
		AlphameticPuzzle puzzle = new(equation);
		PermutationGenerator permutationGenerator = new(puzzle.AddendLetters.Count);
		AlphaDictionary? alphaDictionary;
		do
		{
			if (TrySolve(puzzle, permutationGenerator.Current, out alphaDictionary))
			{
				return alphaDictionary!.ToDictionary();
			}
		}
		while (permutationGenerator.MoveNext());

		string errorMessage = alphaDictionary
			?.Exception
			?.Message
			?? "Unsolvable puzzle.";
		throw new ArgumentException(errorMessage, nameof(equation));
	}

	private static bool TrySolve(AlphameticPuzzle puzzle, double value, out AlphaDictionary alphaDictionary)
	{
		alphaDictionary = new(puzzle.NonZeroLetters);
		try
		{
			WorkingValues workingValues = new(puzzle, alphaDictionary, value);
			for (int columnIndex = 0; workingValues.HasUnusedLetters && columnIndex <= puzzle.PlaceCount; columnIndex++)
			{
				char[] columnLetters = ExtractColumn(puzzle, columnIndex);
				double sum = AssignAddendLetters(columnLetters, workingValues);

				_ = workingValues.FillObviousValues();
				if (!TryAssignSummaryLetter(columnIndex, sum, workingValues, out int carriedValue))
				{
					return false;
				}

				workingValues.CarriedValue = carriedValue;
				_ = workingValues.FillObviousValues();
			}

			return puzzle.Test(workingValues.Dictionary);
		}
		catch (Exception ex)
		{
			alphaDictionary.Exception = ex;
			return false;
		}
	}

	private static double AssignAddendLetters(char[] columnLetters, WorkingValues workingValues)
	{
		AlphaDictionary dictionary = workingValues.Dictionary;
		double sum = workingValues.CarriedValue;

		foreach (char letter in columnLetters)
		{
			if (dictionary.TryGetValue(letter, out int assignedValue))
			{
				sum += assignedValue;
			}
			else if (letter != AlphaDictionary.Placeholder)
			{
				_ = !workingValues.UnusedLetters.Remove(letter);
				bool cantBeZero = !workingValues.NonZeroLetters.Contains(letter);
				int currentValue = workingValues.UnusedValues.Shift(cantBeZero);
				dictionary[letter] = currentValue;
				sum += currentValue;
			}
		}

		return sum;
	}

	private static bool TryAssignSummaryLetter(int columnIndex, double sum, WorkingValues workingValues, out int carriedValue)
	{
		char sumLetter = workingValues.SumWord[columnIndex];
		List<int> sumCharacters = new List<int> { 0 }
			.Concat(sum.ToDigitList())
			.ToList();
		int onesPlace = sumCharacters.Pop();
		carriedValue = sumCharacters.Any()
				? sumCharacters.JoinAsNumber()
				: 0;

		if (!workingValues.IsAssignmentAllowed(sumLetter, onesPlace))
		{
			return false;
		}
		else
		{
			_ = workingValues.Assign(sumLetter, onesPlace);
			return true;
		}
	}

	private static char[] ExtractColumn(AlphameticPuzzle puzzle, int columnIndex)
	{
		List<char> column = new();
		char[][] matrix = puzzle.AddendMatrix;
		for (int rowIndex = 0; rowIndex < matrix.Count(); rowIndex++)
		{
			column.Add(matrix[rowIndex][columnIndex]);
		}

		return column.ToArray();
	}
}