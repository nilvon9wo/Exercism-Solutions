using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

[SuppressMessage("Roslynator", "RCS1110:Declare type inside namespace.", Justification = "Not compatible with tests.")]
[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not compatible with tests.")]
// ReSharper disable once CheckNamespace
public static class Rectangles
{
	private const char _corner = '+';
	private const char _horizontal = '-';
	private const char _vertical = '|';

	public static int Count(string[] diagram)
	{
		_ = diagram ?? throw new ArgumentNullException(nameof(diagram));
		if (!diagram.Any())
		{
			return 0;
		}

		int rowCount = diagram.Length;
		int columnCount = diagram[0].Length;
		IEnumerable<Coordinates> allCoordinates = Enumerable.Range(0, rowCount)
			.SelectMany(
				row => Enumerable.Range(0, columnCount)
					.Select(column => new Coordinates(row, column))
			);

		IEnumerable<Coordinates> allPotentialUpperLeftCorners = FindPotentialUpperLeftCorners(diagram, allCoordinates);
		Coordinates diagramBottomRightCorner = new(rowCount, columnCount);
		return FindRectangles(
				diagram,
				allPotentialUpperLeftCorners,
				diagramBottomRightCorner
			)
			.Count();
	}

	private static IEnumerable<Coordinates> FindPotentialUpperLeftCorners(
		IReadOnlyList<string> diagram, IEnumerable<Coordinates> allCoordinates
	)
		=> allCoordinates
			.Where(upperLeft => diagram[upperLeft.Row][upperLeft.Column] == _corner);

	private static IEnumerable<Rectangle> FindRectangles(
		string[] diagram, IEnumerable<Coordinates> allPotentialUpperLeftCorners, Coordinates diagramBottomRightCorner
	)
		=> allPotentialUpperLeftCorners
			.SelectMany(
				upperLeft => SelectValidRectangles(
					diagram,
					upperLeft,
					diagramBottomRightCorner
				)
			);

	private static IEnumerable<Rectangle> SelectValidRectangles(
		IReadOnlyList<string> diagram,
		Coordinates upperLeft,
		Coordinates diagramBottomRightCorner
	)
		=> Enumerable.Range(upperLeft.Row + 1, diagramBottomRightCorner.Row - upperLeft.Row - 1)
			.SelectMany(
				bottomRow
					=> Enumerable.Range(upperLeft.Column + 1, diagramBottomRightCorner.Column - upperLeft.Column - 1)
						.Select(
							rightColumn =>
								CreateRectangle(diagram, upperLeft, new Coordinates(bottomRow, rightColumn))
						)
						.Where(x => x != null)
			)!;

	private static Rectangle? CreateRectangle(
		IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight
	)
		=> IsValidRectangle(diagram, upperLeft, bottomRight)
			? new Rectangle(upperLeft, bottomRight)
			: null;

	private static bool IsValidRectangle(IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight)
		=> HasThreeMoreCorners(diagram, upperLeft, bottomRight)
		   && HasFourEdges(diagram, upperLeft, bottomRight);

	private static bool HasThreeMoreCorners(
		IReadOnlyList<string> diagram, Coordinates upperLeft, Coordinates bottomRight
	)
		=> diagram[upperLeft.Row][bottomRight.Column] == _corner
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
			.All(column => row[column] is _corner or _horizontal);
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
			.All(row => diagram[row][column] is _corner or _vertical);
	}

	private sealed record Coordinates(int Row, int Column);

	private sealed record Rectangle(Coordinates UpperLeft, Coordinates BottomRight);
}