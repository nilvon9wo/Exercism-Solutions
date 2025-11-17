using System;
﻿using System.Collections.Generic;
using System.Linq;
using System.Reflection;





public class Connect
{
	private readonly Board _board;

	public Connect(string[] inputString)
		=> _board = Board.From(inputString);

	private static readonly Dictionary<Player, Func<Board, Player, bool>> _winningConditionByPlayer
		= new()
		{
			{ Player.White, IsVerticallyConnected },
			{ Player.Black, IsHorizontallyConnected }
		};

	public ConnectWinner Result()
	{
		foreach ((Player player, Func<Board, Player, bool> victoryCondition) in _winningConditionByPlayer)
		{
			if (victoryCondition(_board, player))
			{
				return player.To<ConnectWinner>();
			}
		}

		return ConnectWinner.None;
	}

	private static bool IsVerticallyConnected(Board board, Player player)
	{
		List<Cell> bottomEdge = board.GetBottomEdge();
		List<Cell> topEdge = board.GetTopEdge();
		return IsConnected(board, player, bottomEdge, topEdge);
	}

	private static bool IsHorizontallyConnected(Board board, Player player)
	{
		List<Cell> leftEdge = board.GetLeftEdge();
		List<Cell> rightEdge = board.GetRightEdge();
		return IsConnected(board, player, leftEdge, rightEdge);
	}

	private static bool IsConnected(
			Board board,
			Player player,
			List<Cell> startEdge,
			List<Cell> endEdge
		)
	{
		List<Cell> endCells = endEdge.BelongingTo(player);
		HashSet<HexCoordinates> visited = new();
		return startEdge.BelongingTo(player)
			.Any(startCell =>
				DepthFirstSearch(
					board,
					player,
					startCell.Coordinates,
					endCells,
					visited
				));
	}

	private static bool DepthFirstSearch(
			Board board,
			Player player,
			HexCoordinates currentCoordinates,
			List<Cell> endCells,
			HashSet<HexCoordinates> visited
		)
		=> endCells.Any(cell => cell.Coordinates.Equals(currentCoordinates))
			|| (visited.Add(currentCoordinates) && board.GetNeighbors(currentCoordinates)
				.Any(neighbor =>
					!visited.Contains(neighbor.Coordinates)
					&& neighbor.BelongsTo(player)
					&& DepthFirstSearch(board, player, neighbor.Coordinates, endCells, visited)
				));
}


//=======================================================================



internal static class AttributeExtensions
{
	public static TValue GetValue<TAttribute, TValue>(this TAttribute attribute)
		where TAttribute : Attribute
		where TValue : struct
	{
		_ = attribute ?? throw new ArgumentNullException(nameof(attribute));
		PropertyInfo[] properties = attribute.GetType()
			.GetProperties()
			.Where(property => property.PropertyType == typeof(TValue))
			.ToArray();

		return properties.Length switch
		{
			1
				=> (TValue)properties[0]
					.GetValue(attribute),

			> 1
				=> throw new InvalidOperationException("Attribute value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Attribute value does not have an attribute of the specified type.")
		};
	}
}


//=======================================================================


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



//=======================================================================


﻿internal enum BoardContent
{
	[InputCharacter(' ')]
	Ignored,

	[InputCharacter('.')]
	Empty,

	[InputCharacter('X')]
	Black,

	[InputCharacter('O')]
	White
}


//=======================================================================



internal sealed record Cell(HexCoordinates Coordinates, BoardContent Content)
{
	private static readonly Dictionary<char, BoardContent> _boardContentByCharacter =
		EnumUtilities.ToEnumByAttributeValue<BoardContent, InputCharacterAttribute, char>();

	internal bool BelongsTo(Player player)
		=> Content == player.To<BoardContent>();

	internal static Cell From(char character, int row, int column)
	{
		HexCoordinates coordinates = HexCoordinates.FromAxialCoordinates(column - (row / 2), row);
		return _boardContentByCharacter.TryGetValue(character, out BoardContent contents)
			? new Cell(coordinates, contents)
			: null;
	}
};


//=======================================================================


﻿public enum ConnectWinner
{
	None,
	White,
	Black
}



//=======================================================================


﻿internal enum Direction
{
	TopLeft,
	TopRight,
	BottomLeft,
	BottomRight,
}


//=======================================================================



public static class EnumExtensions
{
	public static T2 To<T2>(this Enum enumValue) where T2 : Enum
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		Type enumType = enumValue.GetType();
		string enumName2 = Enum.GetName(enumType, enumValue);
		return enumName2 != null
			? (T2)Enum.Parse(typeof(T2), enumName2)
			: throw new ArgumentException($"No matching enum value found in {typeof(T2)} for {enumValue}.");
	}

	public static T GetAttributeValue<T>(this Enum enumValue) where T : Attribute
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		FieldInfo fieldInfo = enumValue.GetType()
			.GetField(enumValue.ToString());
		T[] attributes = (T[])fieldInfo
			.GetCustomAttributes(typeof(T), false);

		return attributes.Length switch
		{
			1
				=> attributes[0],

			> 1
				=> throw new InvalidOperationException("Enum value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Enum value does not have an attribute of the specified type.")
		};
	}
}


//=======================================================================



internal static class EnumUtilities
{
	public static Dictionary<TKey, TEnum> ToEnumByAttributeValue<TEnum, TAttribute, TKey>()
		where TEnum : Enum
		where TAttribute : Attribute
		where TKey : struct
		=> Enum.GetValues(typeof(TEnum))
			.Cast<TEnum>()
			.ToDictionary(
				enumValue
					=> enumValue.GetAttributeValue<TAttribute>()
						.GetValue<TAttribute, TKey>(),

				enumValue
					=> enumValue
			);
}



//=======================================================================



internal static class EnumerableExtensions
{
	public static List<Cell> BelongingTo(this IEnumerable<Cell> cells, Player player)
		=> cells.Where(c => c.Content == player.To<BoardContent>())
			.ToList();
}



//=======================================================================



internal readonly struct HexCoordinates
{
	public int Q { get; }
	public int R { get; }
	public int S { get; }

	public HexCoordinates(int q, int r, int s)
	{
		if (q + r + s != 0)
		{
			throw new ArgumentException("Invalid axial coordinates: q + r + s must equal 0.");
		}

		Q = q;
		R = r;
		S = s;
	}

	public static HexCoordinates FromAxialCoordinates(int q, int r)
	{
		int s = -q - r;
		return new HexCoordinates(q, r, s);
	}
}


//=======================================================================



[AttributeUsage(AttributeTargets.Field, AllowMultiple = false)]
internal sealed class InputCharacterAttribute : Attribute
{
	public InputCharacterAttribute(char character)
		=> Character = character;

	public char Character { get; }
}


//=======================================================================


﻿public enum Player
{
	Black,
	White
}



//=======================================================================


