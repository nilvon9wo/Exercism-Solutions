using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class SaddlePoints
{
	[SuppressMessage(
		"Performance",
		"CA1814:Prefer jagged arrays over multidimensional",
		Justification = "Test requirement."
	)]
	public static IEnumerable<(int, int)> Calculate(int[,] multidimensionalMatrix)
	{
		_ = multidimensionalMatrix ?? throw new ArgumentNullException(nameof(multidimensionalMatrix));
		int[][] matrix = multidimensionalMatrix.ToJaggedArray();
		IEnumerable<Tree> highestTrees = FindHighestTrees(matrix);
		return FilterShortestInColumn(matrix, highestTrees)
			.Select(tree => (tree.Coordinates.Row + 1, tree.Coordinates.Column + 1));
	}

	private static IEnumerable<Tree> FindHighestTrees(IEnumerable<int[]> matrix)
		=> matrix.SelectMany(FindHighestTreesInRow);

	private static IEnumerable<Tree> FindHighestTreesInRow(int[] row, int rowIndex)
	{
		int maxHeight = row.Max();
		return row
			.Select((height, columnIndex) => Tree.From(rowIndex, columnIndex, height))
			.Where(tree => tree.Height == maxHeight);
	}

	private static IEnumerable<Tree> FilterShortestInColumn(int[][] matrix, IEnumerable<Tree> highestTrees)
		=> highestTrees.Where(
			tree =>
			{
				int treeHeight = tree.Height;
				int columnIndex = tree.Coordinates.Column;
				int minHeightInColumn = matrix.Min(row => row[columnIndex]);

				return treeHeight == minHeightInColumn;
			}
		);
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

// ReSharper disable once CheckNamespace

internal sealed record Tree(SquareCoordinates Coordinates, int Height)
{
	internal static Tree From(int row, int column, int height)
	{
		SquareCoordinates coordinates = new(row, column);
		return new Tree(coordinates, height);
	}
}

//=======================================================================

[SuppressMessage(
	"Performance",
	"CA1814:Prefer jagged arrays over multidimensional",
	Justification = "Test requirement."
)]
// ReSharper disable once CheckNamespace
public static class ArrayExtensions
{
	public static int[][] ToJaggedArray(this int[,] matrix)
	{
		if (matrix == null)
		{
			throw new ArgumentNullException(nameof(matrix));
		}

		int rowCount = matrix.GetLength(0);
		int columnCount = matrix.GetLength(1);
		return Enumerable.Range(0, rowCount)
			.Select(
				row => TransformRow(matrix, new SquareCoordinates(row, columnCount))
			)
			.ToArray();
	}

	private static int[] TransformRow(int[,] matrix, SquareCoordinates coordinates)
		=> Enumerable.Range(0, coordinates.Column)
			.Aggregate(
				new int[coordinates.Column],
				(accumulator, column) =>
				{
					accumulator[column] = matrix[coordinates.Row, column];
					return accumulator;
				}
			);
}

//=======================================================================