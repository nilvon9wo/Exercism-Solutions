using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class WordSearch
{
	private readonly WordSquare _square;

	public WordSearch(string grid)
		=> _square = WordSquare.From(grid);

	public Dictionary<string, ((int, int), (int, int))?> Search(string[] wordsToSearchFor)
		=> FindWordLocations(wordsToSearchFor)
			.Aggregate(
				new Dictionary<string, ((int, int), (int, int))?>(),
				ConvertValueToNullableDupleTuplePair
			);

	private static Dictionary<string, ((int, int), (int, int))?> ConvertValueToNullableDupleTuplePair(
		Dictionary<string, ((int, int), (int, int))?> locationByWords,
		KeyValuePair<string, List<CartesianCoordinatesRange>> item
	)
	{
		locationByWords[item.Key] = item.Value.Count > 0
			? item.Value.FirstOrDefault()
				.ToDupleTuplePair
			: null;
		return locationByWords;
	}

	private Dictionary<string, List<CartesianCoordinatesRange>> FindWordLocations(string[] wordsToSearchFor)
		=> wordsToSearchFor is null
			? throw new ArgumentNullException(nameof(wordsToSearchFor))
			: wordsToSearchFor.Aggregate(
				new Dictionary<string, List<CartesianCoordinatesRange>>(),
				(results, word) =>
				{
					results[word] = FindWord(word);
					return results;
				}
			);

	private List<CartesianCoordinatesRange> FindWord(string word)
		=> _square.Get(word[0])
			.ToDictionary(
				cell => cell,
				cell => _square.GetNeighbors(cell.Coordinates, word[1])
			)
			.SelectMany(neighborsByFirstCell => GetSequences(neighborsByFirstCell, word))
			.Where(x => x.LetterSequence == word)
			.Select(x => x.CoordinatesRange)
			.ToList();

	private IEnumerable<LetterSequenceLocation> GetSequences(
		KeyValuePair<Cell, Dictionary<Direction, Cell>> neighborsByFirstCell,
		string word
	)
		=> neighborsByFirstCell.Value.Keys
			.ToList()
			.Select(direction => GetSequence(neighborsByFirstCell, direction, word));

	private LetterSequenceLocation GetSequence(
		KeyValuePair<Cell, Dictionary<Direction, Cell>> neighborsByFirstCell,
		Direction direction,
		string word
	)
	{
		CartesianCoordinates firstCellCoordinates = neighborsByFirstCell.Key.Coordinates;
		int length = word.Length;
		return _square.GetSequence(firstCellCoordinates, direction, length);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal enum Direction
{
	Up,
	UpRight,
	Right,
	DownRight,
	Down,
	DownLeft,
	Left,
	UpLeft,
}

//=======================================================================

// ReSharper disable once CheckNamespace

public readonly struct CartesianCoordinates : System.IEquatable<CartesianCoordinates>
{
	public CartesianCoordinates(int x, int y)
	{
		X = x;
		Y = y;
	}

	public int X { get; }

	public int Y { get; }

	public static CartesianCoordinates From((int, int) coordinates)
		=> new(coordinates.Item1, coordinates.Item2);

	public (int, int) ToDupleTuple()
		=> (X, Y);

	public static bool operator ==(CartesianCoordinates left, CartesianCoordinates right)
		=> left.Equals(right);

	public static bool operator !=(CartesianCoordinates left, CartesianCoordinates right)
		=> !(left == right);

	public override bool Equals(object? obj)
		=> obj is CartesianCoordinates coordinates && Equals(coordinates);

	public override int GetHashCode()
		=> System.HashCode.Combine(X, Y);

	public bool Equals(CartesianCoordinates other)
		=> (X == other.X) && (Y == other.Y);
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly record struct CartesianCoordinatesRange(CartesianCoordinates Start, CartesianCoordinates End)
{
	public static implicit operator (CartesianCoordinates, CartesianCoordinates)(CartesianCoordinatesRange value)
		=> (value.Start, value.End);

	public static implicit operator CartesianCoordinatesRange((CartesianCoordinates, CartesianCoordinates) value)
		=> new(value.Item1, value.Item2);

	public ((int, int), (int, int)) ToDupleTuplePair
		=> (Start.ToDupleTuple(), End.ToDupleTuple());
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed record Cell(CartesianCoordinates Coordinates, char Letter)
{
	internal static IEnumerable<Cell> ManyFrom(string input)
	{
		string[] lines = input.Split('\n');
		return Enumerable.Range(0, lines.Length)
			.SelectMany(row => From(lines, row))
			.Aggregate(
				new List<Cell>(),
				(accumulator, cell) =>
				{
					accumulator.Add(cell);
					return accumulator;
				}
			);
	}

	private static IEnumerable<Cell> From(IReadOnlyList<string> lines, int row)
		=> lines[row]
			.Select((letter, column) => From(letter, row, column));

	private static Cell From(char letter, int row, int column)
	{
		CartesianCoordinates coordinates = new(column + 1, row + 1);
		return new(coordinates, letter);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct LetterSequenceLocation
{
	internal string LetterSequence { get; private init; }
	internal CartesianCoordinatesRange CoordinatesRange { get; private init; }

	public static LetterSequenceLocation From(List<Cell> cellSequence)
	{
		char[] letterSequence = cellSequence.Select(cell => cell.Letter)
			.ToArray();
		CartesianCoordinates firstCoordinates = cellSequence[0]
			.Coordinates;
		CartesianCoordinates lastCoordinates = cellSequence.Last()
			.Coordinates;
		return new()
		{
			LetterSequence = new(letterSequence),
			CoordinatesRange = new(firstCoordinates, lastCoordinates),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class WordSquare
{
	private WordSquare() { }

	private Dictionary<CartesianCoordinates, Cell>? _cellByCoordinates;
	private Dictionary<char, List<Cell>>? _cellsByLetter;

	internal static WordSquare From(string inputString)
	{
		List<Cell> cells = Cell.ManyFrom(inputString)
			.ToList();
		return string.IsNullOrEmpty(inputString)
			? throw new ArgumentException("Input string cannot be null or empty.")
			: new()
			{
				_cellByCoordinates = cells
					.ToDictionary<Cell, CartesianCoordinates, Cell>(cell => cell.Coordinates, cell => cell),
				_cellsByLetter = cells
					.GroupBy(x => x.Letter)
					.ToDictionary(x => x.Key, x => x.ToList()),
			};
	}

	internal IEnumerable<Cell> Get(char letter)
		=> (_cellsByLetter != null) && _cellsByLetter.TryGetValue(letter, out List<Cell>? cells)
			? cells
			: new();

	private Cell? GetUnsafe(CartesianCoordinates coordinates)
		=> (_cellByCoordinates != null) && _cellByCoordinates.TryGetValue(coordinates, out Cell? cell)
			? cell
			: null;

	internal Dictionary<Direction, Cell> GetNeighbors(
		CartesianCoordinates unexaminedCoordinate,
		char requiredLetter
	)
		=> GetNeighbors(unexaminedCoordinate)
			.Where(x => x.Value.Letter == requiredLetter)
			.ToDictionary(kvp => kvp.Key, kvp => kvp.Value);

	private Dictionary<Direction, Cell> GetNeighbors(CartesianCoordinates unexaminedCoordinate)
	{
		int x = unexaminedCoordinate.X;
		int y = unexaminedCoordinate.Y;
		int upY = GetUpY(y);
		int downY = GetDownY(y);
		int leftX = GetLeftX(x);
		int rightX = GetRightX(x);
		return new Dictionary<Direction, Cell?>()
			{
				{ Direction.Up, GetUnsafe(new(x, upY)) },
				{ Direction.UpRight, GetUnsafe(new(rightX, upY)) },
				{ Direction.Right, GetUnsafe(new(rightX, y)) },
				{ Direction.DownRight, GetUnsafe(new(rightX, downY)) },
				{ Direction.Down, GetUnsafe(new(x, downY)) },
				{ Direction.DownLeft, GetUnsafe(new(leftX, downY)) },
				{ Direction.Left, GetUnsafe(new(leftX, y)) },
				{ Direction.UpLeft, GetUnsafe(new(leftX, upY)) },
			}
			.Where(kvp => kvp.Value != null)
			.ToDictionary(kvp => kvp.Key, kvp => kvp.Value)!;
	}

	internal LetterSequenceLocation GetSequence(
		CartesianCoordinates firstCoordinates,
		Direction direction,
		int wordLength
	)
	{
		List<Cell> cellSequence = GetSequencedCells(firstCoordinates, direction, wordLength);
		return LetterSequenceLocation.From(cellSequence);
	}

	private List<Cell> GetSequencedCells(CartesianCoordinates firstCoordinates, Direction direction, int wordLength)
	{
		List<Cell> cellSequence = new();
		CartesianCoordinates currentCoordinates = firstCoordinates;
		for (int i = 0; i < wordLength; i++)
		{
			Cell? cell = GetUnsafe(currentCoordinates);
			if (cell == null)
			{
				return cellSequence;
			}

			cellSequence.Add(cell);
			currentCoordinates = MoveCoordinates(currentCoordinates, direction);
		}

		return cellSequence;
	}

	private static CartesianCoordinates MoveCoordinates(CartesianCoordinates currentCoordinates, Direction direction)
	{
		int x = currentCoordinates.X;
		int y = currentCoordinates.Y;
		int upY = GetUpY(y);
		int downY = GetDownY(y);
		int leftX = GetLeftX(x);
		int rightX = GetRightX(x);
		return direction switch
		{
			Direction.Up => new(x, upY),
			Direction.UpRight => new(rightX, upY),
			Direction.Right => new(rightX, y),
			Direction.DownRight => new(rightX, downY),
			Direction.Down => new(x, downY),
			Direction.DownLeft => new(leftX, downY),
			Direction.Left => new(leftX, y),
			Direction.UpLeft => new(leftX, upY),
			_ => throw new ArgumentException("Invalid direction specified."),
		};
	}

	private static int GetRightX(int x)
		=> x + 1;

	private static int GetLeftX(int x)
		=> x - 1;

	private static int GetDownY(int y)
		=> y + 1;

	private static int GetUpY(int y)
		=> y - 1;
}

//=======================================================================