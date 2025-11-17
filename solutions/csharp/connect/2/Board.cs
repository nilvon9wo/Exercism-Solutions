using System.Collections.Generic;
using System.Linq;

internal sealed class Board
{
	private Board() { }
	private Dictionary<HexCoordinates, Cell> _cellByCoordinates;

	internal static Board From(string[] inputString)
		=> inputString == null || inputString.Length == 0
			? throw new global::System.ArgumentException("Input string cannot be null or empty.")
			: new()
			{
				_cellByCoordinates = inputString
					.SelectMany<string, Cell>(Foo)
					.Where<global::Cell>(x => x != null && x.Content != global::BoardContent.Ignored)
					.ToDictionary<global::Cell, global::HexCoordinates, global::Cell>(x => x.Coordinates, x => x)
			};

	private static IEnumerable<Cell> Foo(string rowString, int row)
	{
		bool ignoreSpaces = row % 2 == 0;
		return rowString
			.Where((_, column) =>
				!(ignoreSpaces && column % 2 == 1)
				&& !(!ignoreSpaces && column % 2 == 0)
			)
			.Select((char character, int column) =>
				Cell.From(character, row, column)
			);
	}

	internal Cell Get(HexCoordinates coordinates)
		=> _cellByCoordinates.TryGetValue(coordinates, out Cell cell)
			? cell
			: null;

	internal List<Cell> GetTopEdge()
	{
		int minR = _cellByCoordinates.Keys.Min(c => c.R);
		return _cellByCoordinates.Values
			.Where(cell => cell.Coordinates.R == minR)
			.OrderBy(cell => cell.Coordinates.Q)
			.ToList();
	}

	internal List<Cell> GetBottomEdge()
	{
		int maxR = _cellByCoordinates.Keys.Max(c => c.R);
		return _cellByCoordinates.Values
			.Where(cell => cell.Coordinates.R == maxR)
			.OrderBy(cell => cell.Coordinates.Q)
			.ToList();
	}

	internal List<Cell> GetLeftEdge()
		=> _cellByCoordinates.Values
			.Where(cell =>
				!_cellByCoordinates.ContainsKey(new HexCoordinates(cell.Coordinates.Q - 1, cell.Coordinates.R, cell.Coordinates.S + 1)))
			.GroupBy(cell => cell.Coordinates.R)
			.Select(group => group.OrderBy(cell => cell.Coordinates.Q).First())
			.OrderBy(cell => cell.Coordinates.R)
			.ToList();

	internal List<Cell> GetRightEdge()
		=> _cellByCoordinates.Values
			.Where(cell =>
				!_cellByCoordinates.ContainsKey(new HexCoordinates(cell.Coordinates.Q + 1, cell.Coordinates.R, cell.Coordinates.S - 1)))
			.GroupBy(cell => cell.Coordinates.R)
			.Select(group => group.OrderByDescending(cell => cell.Coordinates.Q).First())
			.OrderBy(cell => cell.Coordinates.R)
			.ToList();

	internal IEnumerable<Cell> GetNeighbors(HexCoordinates coordinates)
		=> new List<HexCoordinates>
		{
				new (coordinates.Q - 1, coordinates.R, coordinates.S + 1),
				new (coordinates.Q - 1, coordinates.R + 1, coordinates.S),
				new (coordinates.Q, coordinates.R - 1, coordinates.S + 1),
				new (coordinates.Q, coordinates.R + 1, coordinates.S - 1),
				new (coordinates.Q + 1, coordinates.R - 1, coordinates.S),
				new (coordinates.Q + 1, coordinates.R, coordinates.S - 1)
		}
		.Select(Get)
		.Where(neighborCell => neighborCell != null);
}
