using System.Collections.Generic;

internal record class Cell(HexCoordinates Coordinates, BoardContent Content)
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