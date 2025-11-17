using System.Collections.Generic;
using System.Linq;
using System.Text;

public static class Minesweeper
{
	private const char _mine = '*';
	private const char _empty = ' ';

	public static string[] Annotate(string[] minefield)
	{
		char[][] minefieldMatrix = minefield
			.Select(x => x.ToArray())
			.ToArray();

		List<string> result = new();
		foreach (int row in minefield.Length.ToRange())
		{
			StringBuilder stringBuilder = new();
			foreach (int column in minefield[row].Length.ToRange())
			{
				char annotation = (minefieldMatrix[row][column] == _mine)
					? _mine
					: IsAdjacentToMines(minefieldMatrix, row, column, out int mineCount)
						? mineCount.ToChar()
						: _empty;

				stringBuilder = stringBuilder.Append(annotation);
			}

			result.Add(stringBuilder.ToString());
		}

		return result.ToArray();
	}

	private static bool IsAdjacentToMines(char[][] minefieldMatrix, int currentRow, int currentColumn, out int mineCount)
	{
		mineCount = CountAdjacentMines(minefieldMatrix, currentRow, currentColumn);
		return mineCount != 0;
	}

	private static int CountAdjacentMines(char[][] minefieldMatrix, int currentRow, int currentColumn)
	{
		int mineCount = 0;
		for (int row = currentRow - 1; row <= currentRow + 1; row++)
		{
			if (row >= 0 && row < minefieldMatrix.Length)
			{
				mineCount += CountAdjacentMines(minefieldMatrix[row], currentColumn);
			}
		}

		return mineCount;
	}

	private static int CountAdjacentMines(char[] inspectionRow, int currentColumn)
	{
		int mineCount = 0;
		for (int column = currentColumn - 1; column <= currentColumn + 1; column++)
		{
			if (
				column >= 0
					&& column < inspectionRow.Length
					&& inspectionRow[column] == _mine
				)
			{
				mineCount++;
			}
		}

		return mineCount;
	}
}

public static class IntegerExtensions
{
	public static IEnumerable<int> ToRange(this int value) =>
		Enumerable.Range(0, value);

	public static char ToChar(this int value)
		=> $"{value}".ToCharArray()[0];
}