using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

// ReSharper disable once CheckNamespace
public static class OcrNumbers
{
	private static readonly Dictionary<string, int> _valueBySignature
		= Digits.Numbers
			.Select((display, index) => new { Key = index, Value = display })
			.ToDictionary(
				item => GridTokenFactory.CreateSignature(item.Value),
				item => item.Key
			);

	public static string Convert(string inputString)
	{
		_ = inputString ?? throw new ArgumentNullException(nameof(inputString));
		Display display = Display.From(inputString);
		List<StringBuilder> thousandsBuilder = Enumerable.Range(0, display.MaxRow)
			.Where(row => row % Digits.CharacterHeight == 0)
			.Select(row => ConvertRow(display, row))
			.ToList();
		return string.Join(",", thousandsBuilder.Select(sb => sb.ToString()));
	}

	private static StringBuilder ConvertRow(Display display, int row)
		=> Enumerable.Range(0, display.MaxColumn)
			.Where(column => column % Digits.CharacterWidth == 0)
			.Select(
				column =>
				{
					string token = GridTokenFactory.CreateGridToken(display, new SquareCoordinates(row, column));
					return _valueBySignature.TryGetValue(token, out int digit)
						? $"{digit}"
						: "?";
				}
			)
			.Aggregate(new StringBuilder(), (rowBuilder, digitStr) => rowBuilder.Append(digitStr));
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class Digits
{
	public const int CharacterWidth = 3;
	public const int CharacterHeight = 4;

	private static readonly Display _zero = Display.From(
		" _ \n" +
		"| |\n" +
		"|_|\n" +
		"   "
	);

	private static readonly Display _one = Display.From(
		"   \n" +
		"  |\n" +
		"  |\n" +
		"   "
	);

	private static readonly Display _two = Display.From(
		" _ \n" +
		" _|\n" +
		"|_ \n" +
		"   "
	);

	private static readonly Display _three = Display.From(
		" _ \n" +
		" _|\n" +
		" _|\n" +
		"   "
	);

	private static readonly Display _four = Display.From(
		"   \n" +
		"|_|\n" +
		"  |\n" +
		"   "
	);

	private static readonly Display _five = Display.From(
		" _ \n" +
		"|_ \n" +
		" _|\n" +
		"   "
	);

	private static readonly Display _six = Display.From(
		" _ \n" +
		"|_ \n" +
		"|_|\n" +
		"   "
	);

	private static readonly Display _seven = Display.From(
		" _ \n" +
		"  |\n" +
		"  |\n" +
		"   "
	);

	private static readonly Display _eight = Display.From(
		" _ \n" +
		"|_|\n" +
		"|_|\n" +
		"   "
	);

	private static readonly Display _nine = Display.From(
		" _ \n" +
		"|_|\n" +
		" _|\n" +
		"   "
	);

	internal static readonly Display[] Numbers = new Display[]
	{
		_zero,
		_one,
		_two,
		_three,
		_four,
		_five,
		_six,
		_seven,
		_eight,
		_nine
	};
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class GridTokenFactory
{
	private const int _characterWidth = Digits.CharacterWidth;
	private const int _characterHeight = Digits.CharacterHeight;
	private static readonly Regex _ignoredPattern = new("[\r\n]+", RegexOptions.Compiled);

	internal static string CreateSignature(Display display)
		=> CreateGridToken(display, new SquareCoordinates(0, 0));

	internal static string CreateGridToken(Display display, SquareCoordinates startCoordinates)
	{
		Cell startCell = display.Get(startCoordinates) ??
						 throw new ArgumentException("Invalid coordinates. Cell does not exist.");
		IEnumerable<string> grid = CreateSubGrid(display, startCell)
			.Select(characters => new string(characters));
		string token = string.Concat(grid);
		return _ignoredPattern.Replace(token, "");
	}

	private static IEnumerable<char[]> CreateSubGrid(Display display, Cell startCell)
		=> Enumerable.Range(0, 4)
			.Aggregate(
				JaggedArrayFactory.CreateJaggedArray<char>(_characterHeight, _characterWidth),
				(grid, row) => FillGridRow(
					grid,
					display,
					startCell,
					row
				)
			);

	private static char[][] FillGridRow(
		char[][] grid, Display display, Cell startCell,
		int row
	)
	{
		grid[row] = Enumerable.Range(0, _characterWidth)
			.Select(column => GetCharacter(display, startCell, new SquareCoordinates(row, column)))
			.ToArray();
		return grid;
	}

	private static char GetCharacter(Display display, Cell startCell, SquareCoordinates relativePosition)
	{
		int newRow = startCell.Coordinates.Row + relativePosition.Row;
		int newColumn = startCell.Coordinates.Column + relativePosition.Column;
		SquareCoordinates newCoordinates = new(newRow, newColumn);
		return display.Get(newCoordinates)
				   ?.DisplayCharacter ??
			   throw new ArgumentException($"Display content insufficient to generate token with from {startCell}.");
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class JaggedArrayFactory
{
	internal static T[][] CreateJaggedArray<T>(int rowCount, int columnCount)
		=> Enumerable.Range(0, rowCount)
			.Select(_ => new T[columnCount])
			.ToArray();
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed record Cell(SquareCoordinates Coordinates, char DisplayCharacter)
{
	internal static IEnumerable<Cell> ManyFrom(string rowString, int row)
		=> rowString
			.Select(
				(character, column) =>
					From(character, row, column)
			)
			.Where(character => character != null)!;

	private static Cell? From(char character, int row, int column)
	{
		SquareCoordinates coordinates = new(row, column);
		return new Cell(coordinates, character);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class Display
{
	private Display() { }

	private const int _characterWidth = Digits.CharacterWidth;
	private const int _characterHeight = Digits.CharacterHeight;
	private Dictionary<SquareCoordinates, Cell>? _cellByCoordinates;

	internal static Display From(string inputString)
	{
		if (inputString.Length == 0)
		{
			throw new ArgumentException("Input string cannot be null or empty.");
		}

		List<Cell> cells = inputString
			.Split('\n')
			.SelectMany(Cell.ManyFrom)
			.ToList();

		return new Display
		{
			_cellByCoordinates = Validate(cells)
				.ToDictionary(x => x.Coordinates, x => x)
		};
	}

	private IEnumerable<Cell> _cells
		=> _cellByCoordinates!
			.Values;

	public int MaxRow
		=> _cells.Max(cell => cell.Coordinates.Row);

	public int MaxColumn
		=> _cells.Max(cell => cell.Coordinates.Column);

	private static IEnumerable<Cell> Validate(IReadOnlyCollection<Cell> cells)
	{
		_ = ValidateRows(cells) && ValidateColumns(cells);
		return cells;
	}

	private static bool ValidateRows(IEnumerable<Cell> cells)
	{
		int count = cells.Max(cell => cell.Coordinates.Row) + 1;
		return count % _characterHeight != 0
			? throw new ArgumentException($"Row count must be divisible by 4; {count} rows found.")
			: true;
	}

	private static bool ValidateColumns(IEnumerable<Cell> cells)
	{
		int count = cells.Max(cell => cell.Coordinates.Column) + 1;
		return count % _characterWidth != 0
			? throw new ArgumentException($"Column count must be divisible by 3; {count} rows found.")
			: true;
	}

	internal Cell? Get(SquareCoordinates coordinates)
		=> _cellByCoordinates != null && _cellByCoordinates.TryGetValue(coordinates, out Cell? cell)
			? cell
			: null;
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct SquareCoordinates
{
	public SquareCoordinates(int row, int column)
	{
		Row = row;
		Column = column;
	}

	public int Row { get; }
	public int Column { get; }
}

//=======================================================================