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

public class CartesianCoordinates
{
	public CartesianCoordinates(int x, int y)
	{
		X = x;
		Y = y;
	}

	public int X { get; }

	public int Y { get; }

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
		Dictionary<int, List<Cell>> cellsByRowIndex = _cellByCoordinates!.Values
			.GroupBy(cell => cell.Coordinates.Y)
			.ToDictionary(x => x.Key, x => x.ToList());
		return cellsByRowIndex.Keys.Any()
			? string.Join("\n", CreateRowStrings(cellsByRowIndex))
			: string.Empty;
	}

	private static IEnumerable<string> CreateRowStrings(Dictionary<int, List<Cell>> cellsByRowIndex)
		=> Enumerable.Range(1, cellsByRowIndex.Keys.Max())
			.Aggregate(
				new List<string>(),
				(aggregate, y) =>
				{
					List<Cell> rowCells = cellsByRowIndex[y];
					Dictionary<int, char> lettersByColumnIndex = rowCells.ToDictionary(
						cell => cell.Coordinates.X,
						cell => cell.Letter
					);
					string newRowContent = CreateRowString(lettersByColumnIndex);
					aggregate.Add(newRowContent);
					return aggregate;
				}
			);

	private static string CreateRowString(Dictionary<int, char> rowLettersByColumnIndex)
		=> Enumerable.Range(1, rowLettersByColumnIndex.Keys.Max())
			.Aggregate(
				new StringBuilder(),
				(aggregate, x) =>
				{
					char? nextCharacter = rowLettersByColumnIndex.TryGetValue(x, out char letter)
						? letter
						: ' ';
					return aggregate.Append(nextCharacter);
				}
			)
			.ToString();
}

//=======================================================================