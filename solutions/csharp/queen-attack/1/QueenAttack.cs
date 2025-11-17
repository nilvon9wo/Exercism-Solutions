using System;

// ReSharper disable once CheckNamespace
public class Queen
{
	public Queen(int row, int column)
	{
		if (row is < 0 or > 7)
		{
			throw new ArgumentOutOfRangeException(nameof(row), "Row does not exist.");
		}

		if (column is < 0 or > 7)
		{
			throw new ArgumentOutOfRangeException(nameof(column), "Column does not exist.");
		}

		Row = row;
		Column = column;
	}

	public int Row { get; }
	public int Column { get; }
}

// ReSharper disable once CheckNamespace
public static class QueenAttack
{
	public static bool CanAttack(Queen white, Queen black)
		=> white is null
			? throw new ArgumentNullException(nameof(white))
			: black is null
				? throw new ArgumentNullException(nameof(black))
				: AreInSameRow(white, black) ||
				  AreInSameColumn(white, black) ||
				  AreDiagonal(white, black);

	private static bool AreDiagonal(Queen white, Queen black)
		=> Math.Abs(white.Row - black.Row) == Math.Abs(white.Column - black.Column);

	private static bool AreInSameColumn(Queen white, Queen black)
		=> white.Column == black.Column;

	private static bool AreInSameRow(Queen white, Queen black)
		=> white.Row == black.Row;

	public static Queen Create(int row, int column)
		=> new(row, column);
}