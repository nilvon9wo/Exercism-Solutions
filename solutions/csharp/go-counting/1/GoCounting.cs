using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

// ReSharper disable once CheckNamespace
public class GoCounting
{
	private readonly Board _board;

	public GoCounting(string input)
	{
		if (string.IsNullOrEmpty(input))
		{
			throw new ArgumentException($"'{nameof(input)}' cannot be null or whitespace.", nameof(input));
		}

		_board = Board.From(input);
	}

	public Tuple<Owner, HashSet<(int, int)>> Territory((int, int) coordinates)
	{
		CartesianCoordinates boardCoordinates = CartesianCoordinates.From(coordinates);
		return Territory(boardCoordinates);
	}

	private Tuple<Owner, HashSet<(int, int)>> Territory(CartesianCoordinates coordinates)
	{
		HashSet<Cell> delimitedArea = AreaFinder.FindDelimitedArea(_board, coordinates);
		HashSet<Owner> owners = delimitedArea.Select(cell => cell.Owner)
			.Where(owner => owner != Owner.None)
			.ToHashSet();
		Owner owner = owners.Count != 1
			? Owner.None
			: owners.FirstOrDefault();

		HashSet<(int, int)> coordinateTuples = delimitedArea
			.Where(cell => !cell.HasOwner)
			.Select(cell => cell.Coordinates.ToDupleTuple())
			.ToHashSet();
		return Tuple.Create(owner, coordinateTuples);
	}

	public Dictionary<Owner, HashSet<(int, int)>> Territories()
	{
		List<CartesianCoordinates> unexaminedCoordinates = _board.GetAll()
			.Select(cell => cell.Coordinates)
			.ToList();

		Dictionary<Owner, HashSet<(int, int)>> cellsByOwners = new()
		{
			{ Owner.Black, new() }, { Owner.White, new() }, { Owner.None, new() },
		};
		while (unexaminedCoordinates.Count > 0)
		{
			CartesianCoordinates unexaminedCoordinate = unexaminedCoordinates.Pop();
			(Owner owner, HashSet<(int, int)>? territoryCoordinates) = Territory(unexaminedCoordinate);
			if (cellsByOwners.TryGetValue(owner, out HashSet<(int, int)>? ownerCoordinates))
			{
				cellsByOwners[owner] = ownerCoordinates.Concat(territoryCoordinates)
					.ToHashSet();
			}
		}

		return cellsByOwners;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
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
					.GetValue(attribute)!,

			> 1
				=> throw new InvalidOperationException(
					"Attribute value has multiple attributes of the specified type."
				),

			_
				=> throw new InvalidOperationException(
					"Attribute value does not have an attribute of the specified type."
				),
		};
	}
}

//=======================================================================

[AttributeUsage(AttributeTargets.Field)]
internal sealed class InputCharacterAttribute : Attribute
{
	public InputCharacterAttribute(char character)
		=> Character = character;

	public char Character { get; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
public static class EnumExtensions
{
	public static T2 To<T2>(this Enum enumValue) where T2 : Enum
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		Type enumType = enumValue.GetType();
		string? enumName2 = Enum.GetName(enumType, enumValue);
		return enumName2 != null
			? (T2)Enum.Parse(typeof(T2), enumName2)
			: throw new ArgumentException($"No matching enum value found in {typeof(T2)} for {enumValue}.");
	}

	public static T GetAttributeValue<T>(this Enum enumValue) where T : Attribute
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		FieldInfo? fieldInfo = enumValue.GetType()
			.GetField(enumValue.ToString());
		T[] attributes = (T[])fieldInfo
			?.GetCustomAttributes(typeof(T), false)!;

		return attributes.Length switch
		{
			1
				=> attributes[0],

			> 1
				=> throw new InvalidOperationException("Enum value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Enum value does not have an attribute of the specified type."),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
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

// ReSharper disable once CheckNamespace

public enum Owner
{
	[InputCharacter(' ')]
	None,

	[InputCharacter('B')]
	Black,

	[InputCharacter('W')]
	White,
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class Board
{
	private Board() { }

	private Dictionary<CartesianCoordinates, Cell>? _cellByCoordinates;

	internal static Board From(string inputString)
		=> string.IsNullOrEmpty(inputString)
			? throw new System.ArgumentException("Input string cannot be null or empty.")
			: new()
			{
				_cellByCoordinates = Cell.ManyFrom(inputString)
					.ToDictionary(x => x.Coordinates, x => x),
			};

	internal int MaxX
		=> _cellByCoordinates!.Values
			.Max(cell => cell.Coordinates.X);

	internal int MaxY
		=> _cellByCoordinates!.Values
			.Max(cell => cell.Coordinates.Y);

	internal Cell Get(CartesianCoordinates coordinates)
		=> (_cellByCoordinates != null) && _cellByCoordinates.TryGetValue(coordinates, out Cell? cell)
			? cell
			: throw new ArgumentException("Coordinates do not exist", nameof(coordinates));

	private Cell? GetUnsafe(CartesianCoordinates coordinates)
		=> (_cellByCoordinates != null) && _cellByCoordinates.TryGetValue(coordinates, out Cell? cell)
			? cell
			: null;

	internal HashSet<Cell> GetAll()
		=> _cellByCoordinates!.Values.ToHashSet();

	internal IEnumerable<Cell> GetNeighbors(CartesianCoordinates unexaminedCoordinate)
	{
		int x = unexaminedCoordinate.X;
		int y = unexaminedCoordinate.Y;
		return new HashSet<Cell?>()
			{
				GetUnsafe(new(x - 1, y)),
				GetUnsafe(new(x + 1, y)),
				GetUnsafe(new(x, y + 1)),
				GetUnsafe(new(x, y - 1)),
			}
			.Where(cell => cell != null)
			.ToHashSet()!;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct CartesianCoordinates
{
	public CartesianCoordinates(int row, int column)
	{
		X = row;
		Y = column;
	}

	public int X { get; }
	public int Y { get; }

	public static CartesianCoordinates From((int, int) coordinates)
		=> new(coordinates.Item1, coordinates.Item2);

	public (int, int) ToDupleTuple()
		=> (X, Y);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed record Cell(CartesianCoordinates Coordinates, Owner Owner)
{
	private static readonly Dictionary<char, Owner> _ownerByCharacter =
		EnumUtilities.ToEnumByAttributeValue<Owner, InputCharacterAttribute, char>();

	internal bool HasOwner
		=> Owner != Owner.None;

	internal static IEnumerable<Cell> ManyFrom(string input)
		=> input.Split('\n')
			.SelectMany(
				(line, row) =>
					line.Select((character, column) => From(character, row, column))
			)
			.Select(cell => cell);

	private static Cell From(char character, int row, int column)
	{
		CartesianCoordinates coordinates = new(column, row);
		return _ownerByCharacter.TryGetValue(character, out Owner contents)
			? new Cell(coordinates, contents)
			: throw new System.ArgumentException($"Unexpected character {character}.");
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class AreaFinder
{
	internal static HashSet<Cell> FindDelimitedArea(Board board, CartesianCoordinates coordinates)
	{
		Cell inputCell = board.Get(coordinates);
		if (inputCell.HasOwner)
		{
			return new();
		}

		List<CartesianCoordinates> unexaminedCoordinates = new() { coordinates };
		HashSet<Cell> areaCells = new() { inputCell };
		while (unexaminedCoordinates.Count > 0)
		{
			CartesianCoordinates unexaminedCoordinate = unexaminedCoordinates.Pop();
			HashSet<Cell> neighbors = board.GetNeighbors(unexaminedCoordinate)
				.ToHashSet();
			IEnumerable<CartesianCoordinates> newUnexaminedCoordinates = neighbors
				.Where(neighbor => !neighbor.HasOwner)
				.Except(areaCells)
				.Select(cell => cell.Coordinates);
			unexaminedCoordinates = unexaminedCoordinates.Union(newUnexaminedCoordinates)
				.ToList();
			areaCells = areaCells.Concat(neighbors)
				.ToHashSet();
		}

		return areaCells;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public static class ListExtensions
{
	public static T Pop<T>(this List<T> list)
	{
		if (list == null)
		{
			throw new ArgumentNullException(nameof(list));
		}

		if (list.Count == 0)
		{
			throw new InvalidOperationException("The list is empty.");
		}

		T poppedItem = list.Last();
		list.RemoveAt(list.Count - 1);
		return poppedItem;
	}
}

//=======================================================================