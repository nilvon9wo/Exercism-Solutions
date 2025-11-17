using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Rectangles
{
	public static int Count(string[] diagram)
	{
		_ = diagram ?? throw new ArgumentNullException(nameof(diagram));
		return !diagram.Any()
			? 0
			: RectangleFinder.FindRectangles(diagram)
				.Count();
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class RectangleFinder
{
	internal static IEnumerable<Rectangle> FindRectangles(string[] diagram)
	{
		int rowCount = diagram.Length;
		int columnCount = diagram[0].Length;
		IEnumerable<Coordinates> potentialUpperLeftCorners = CollectAllCoordinates(diagram);
		Coordinates diagramBottomRightCorner = new(rowCount, columnCount);
		return potentialUpperLeftCorners
			.SelectMany(upperLeft => FindRectangles(diagram, upperLeft, diagramBottomRightCorner));
	}

	private static IEnumerable<Rectangle> FindRectangles(
		IReadOnlyList<string> diagram,
		Coordinates potentialUpperLeftCorner,
		Coordinates diagramBottomRightCorner
	)
	{
		IEnumerable<Coordinates> potentialBottomRightCorners = Enumerable
			.Range(potentialUpperLeftCorner.Row + 1, diagramBottomRightCorner.Row - potentialUpperLeftCorner.Row - 1)
			.SelectMany(
				bottomRow
					=> Enumerable.Range(
							potentialUpperLeftCorner.Column + 1,
							diagramBottomRightCorner.Column - potentialUpperLeftCorner.Column - 1
						)
						.Select(
							rightColumn => new Coordinates(bottomRow, rightColumn)
						)
			);

		return potentialBottomRightCorners
			.Where(bottomRight => RectangleValidator.IsValid(diagram, potentialUpperLeftCorner, bottomRight))
			.Select(bottomRight => new Rectangle(potentialUpperLeftCorner, bottomRight));
	}

	private static IEnumerable<Coordinates> CollectAllCoordinates(string[] diagram)
	{
		int rowCount = diagram.Length;
		int columnCount = diagram[0].Length;
		return Enumerable
			.Range(0, rowCount)
			.SelectMany(
				row => Enumerable.Range(0, columnCount)
					.Select(column => new Coordinates(row, column))
			);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class RectangleValidator
{
	private const char _corner = Symbols.Corner;

	internal static bool IsValid(IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight)
		=> HasFourCorners(diagram, upperLeft, bottomRight)
		   && HasFourEdges(diagram, upperLeft, bottomRight);

	private static bool HasFourCorners(
		IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight
	)
		=> diagram[upperLeft.Row][upperLeft.Column] == _corner
		   && diagram[upperLeft.Row][bottomRight.Column] == _corner
		   && diagram[bottomRight.Row][upperLeft.Column] == _corner
		   && diagram[bottomRight.Row][bottomRight.Column] == _corner;

	private static bool HasFourEdges(IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight)
	{
		Coordinates upperRight = new(upperLeft.Row, bottomRight.Column);
		Coordinates bottomLeft = new(bottomRight.Row, upperLeft.Column);
		return HasHorizontalEdge(diagram, upperLeft, upperRight)
			   && HasHorizontalEdge(diagram, bottomLeft, bottomRight)
			   && HasVerticalEdge(diagram, upperLeft, bottomLeft)
			   && HasVerticalEdge(diagram, upperRight, bottomRight);
	}

	private static bool HasHorizontalEdge(
		IReadOnlyList<string> diagram, Coordinates leftCoordinates, Coordinates rightCoordinates
	)
	{
		if (leftCoordinates.Row != rightCoordinates.Row)
		{
			throw new InvalidOperationException(
				$"Left {leftCoordinates} and Right {rightCoordinates} are not on the same row."
			);
		}

		string row = diagram[leftCoordinates.Row];
		int startColumn = leftCoordinates.Column;
		int endColumn = rightCoordinates.Column;
		return Enumerable.Range(startColumn + 1, endColumn - startColumn - 1)
			.All(column => row[column] is _corner or Symbols.Horizontal);
	}

	private static bool HasVerticalEdge(
		IReadOnlyList<string> diagram, Coordinates upperCoordinates, Coordinates lowerCoordinates
	)
	{
		if (upperCoordinates.Column != lowerCoordinates.Column)
		{
			throw new InvalidOperationException(
				$"Upper {upperCoordinates} and Lower {lowerCoordinates} are not in same column."
			);
		}

		int column = upperCoordinates.Column;
		int startRow = upperCoordinates.Row;
		int endRow = lowerCoordinates.Row;
		return Enumerable.Range(startRow + 1, endRow - startRow - 1)
			.All(row => diagram[row][column] is _corner or Symbols.Vertical);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class Symbols
{
	internal const char Corner = '+';
	internal const char Horizontal = '-';
	internal const char Vertical = '|';
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct Coordinates
{
	public Coordinates(int row, int column)
	{
		Row = row;
		Column = column;
	}

	public int Row { get; }
	public int Column { get; }
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct Rectangle
{
	public Rectangle(Coordinates upperLeft, Coordinates bottomRight)
	{
		UpperLeft = upperLeft;
		BottomRight = bottomRight;
	}

	public Coordinates UpperLeft { get; }
	public Coordinates BottomRight { get; }
}

//=======================================================================