using System.Collections.Generic;
using System.Linq;

public class WorkingValues
{
	public WorkingValues(AlphameticPuzzle puzzle, AlphaDictionary dictionary, double value)
	{
		Dictionary = dictionary;
		AddendLetters = puzzle.AddendLetters;
		NonZeroLetters = puzzle.NonZeroLetters;
		SumWord = puzzle.SumWord;
		UnusedLetters = new(puzzle.ActiveLetters);
		UnusedValues = value.ToDigitList();
	}

	public AlphaDictionary Dictionary { get; init; }

	public HashSet<char> AddendLetters { get; init; }

	public HashSet<char> NonZeroLetters { get; init; }

	public char[] SumWord { get; init; }

	public int CarriedValue { get; set; } = 0;

	public List<int> UnusedValues { get; init; }

	public HashSet<char> UnusedLetters { get; init; }

	public bool HasUnusedLetters =>
		UnusedLetters.Any();

	private bool _hasOneValueLeft
		=> UnusedValues.Count == 1 && UnusedLetters.Count == 1;

	public AlphaDictionary FillObviousValues()
	{
		if (_hasOneValueLeft)
		{
			char remainingLetter = UnusedLetters.Shift();
			int remainingValue = UnusedValues.Shift();
			Dictionary[remainingLetter] = remainingValue;
		}

		return Dictionary;
	}

	public bool IsAssignmentAllowed(char character, int newValue)
		=> IsAlreadyAssigned(character, newValue)
		||
		(
			!Dictionary.Values.Contains(newValue)
			&& FitsAddendRequirements(character, newValue)
		);

	private bool FitsAddendRequirements(char character, int newValue) =>
		(AddendLetters.Contains(character) && UnusedValues.Contains(newValue))
			|| (!AddendLetters.Contains(character) && !UnusedValues.Contains(newValue));

	private bool IsAlreadyAssigned(char character, double newValue) =>
		Dictionary.TryGetValue(character, out int assignedValue)
							&& assignedValue == newValue;

	public AlphaDictionary Assign(char character, double value)
	{
		Dictionary[character] = (int)value;
		_ = UnusedLetters.Remove(character);
		_ = UnusedValues.Remove((int)value);
		return Dictionary;
	}
}
