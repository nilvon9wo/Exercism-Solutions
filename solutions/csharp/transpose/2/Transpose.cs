using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;

// ReSharper disable once CheckNamespace
public static class Transpose
{
	[SuppressMessage("Naming", "CA1720:Identifier contains type name", Justification = "Required by test.")]
	public static string String(string input)
	{
		_ = input ?? throw new ArgumentException($"'{nameof(input)}' cannot be null.", nameof(input));
		return LetterGrid.From(input)
			.Transpose()
			.ToString();
	}
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

	public CartesianCoordinates Transpose()
		=> new(Y, X);
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

	internal Cell Transpose()
		=> new(Coordinates.Transpose(), Letter);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class LetterGrid
{
	private Dictionary<CartesianCoordinates, Cell>? _cellByCoordinates;

	internal static LetterGrid From(string input)
	{
		_ = input ?? throw new ArgumentException($"'{nameof(input)}' cannot be null.", nameof(input));
		return new()
		{
			_cellByCoordinates = Cell.ManyFrom(input)
				.ToList()
				.ToDictionary(cell => cell.Coordinates, cell => cell),
		};
	}

	internal LetterGrid Transpose()
		=> new()
		{
			_cellByCoordinates = _cellByCoordinates!.Values
				.Select(cell => cell.Transpose())
				.ToDictionary(cell => cell.Coordinates, cell => cell),
		};

	public override string ToString()
	{
		Dictionary<int, List<CartesianCoordinates>> coordinatesByRowIndex = _cellByCoordinates!.Values
			.GroupBy(cell => cell.Coordinates.Y)
			.ToDictionary(
				group => group.Key,
				group => group.Select(x => x.Coordinates)
					.ToList()
			);
		return coordinatesByRowIndex.Keys.Any()
			? string.Join("\n", CreateRowStrings(coordinatesByRowIndex))
			: string.Empty;
	}

	private IEnumerable<string> CreateRowStrings(Dictionary<int, List<CartesianCoordinates>> coordinatesByRowIndex)
		=> Enumerable.Range(1, coordinatesByRowIndex.Keys.Max())
			.Aggregate(
				new List<string>(),
				(aggregate, y) =>
				{
					aggregate.Add(CreateRowString(coordinatesByRowIndex[y]));
					return aggregate;
				}
			);

	private string CreateRowString(IReadOnlyCollection<CartesianCoordinates> rowCells)
	{
		int y = rowCells.First()
			.Y;
		return Enumerable.Range(1, rowCells.Max(cell => cell.X))
			.Aggregate(
				new StringBuilder(),
				(aggregate, x) => aggregate.Append(GetCharacterFor(x, y))
			)
			.ToString();
	}

	private char? GetCharacterFor(int x, int y)
		=> _cellByCoordinates!.TryGetValue(new(x, y), out Cell? cell)
			? cell.Letter
			: ' ';
}

//=======================================================================