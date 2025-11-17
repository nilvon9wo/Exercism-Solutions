using System;
using System.Collections.Generic;
using System.Linq;

public static class Rectangles
{
    public static int Count(string[] diagram)
    {
        if (!diagram.Any())
        {
            return 0;
        }
        
        int rowCount = diagram.Length;
        int columnCount = diagram[0].Length;
        return Enumerable.Range(0, rowCount)
            .SelectMany(row => Enumerable.Range(0, columnCount)
                .Select(column => new Coordinate(row, column)))
            .Where(upperLeft =>  
                diagram[upperLeft.Row][upperLeft.Column] == '+'
            )
            .SelectMany(upperLeft 
                => SelectValidRectangles(diagram, upperLeft, rowCount, columnCount)
            )
            .Count();
    }

    private static IEnumerable<int> SelectValidRectangles(
                IReadOnlyList<string> diagram, 
                Coordinate upperLeft, 
                int rowCount, 
                int colCount
            ) 
        => Enumerable.Range(upperLeft.Row + 1, rowCount - upperLeft.Row - 1)
            .SelectMany(bottomRow 
                => Enumerable.Range(upperLeft.Column + 1, colCount - upperLeft.Column - 1)
                    .Where(IsValidRectangle(diagram, upperLeft, bottomRow)
                )
            );

    private static Func<int, bool> IsValidRectangle(IReadOnlyList<string> diagram, Coordinate upperLeft, int bottomRow) 
        => rightColumn 
            => HasThreeMoreCorners(diagram, upperLeft, new Coordinate(bottomRow, rightColumn))
                && HasFourEdges(diagram, upperLeft, new Coordinate(bottomRow, rightColumn));

    private static bool HasThreeMoreCorners(IReadOnlyList<string> diagram, Coordinate upperLeft, Coordinate bottomRight) 
        => diagram[upperLeft.Row][bottomRight.Column] == '+' &&
        diagram[bottomRight.Row][upperLeft.Column] == '+' &&
        diagram[bottomRight.Row][bottomRight.Column] == '+';
    
    private static bool HasFourEdges(IReadOnlyList<string> diagram, Coordinate upperLeft, Coordinate bottomRight)
    {
        Coordinate upperRight = new(upperLeft.Row, bottomRight.Column);
        Coordinate bottomLeft = new(bottomRight.Row, upperLeft.Column);
        return HasHorizontalEdge(diagram, upperLeft, upperRight)
               && HasHorizontalEdge(diagram, bottomLeft, bottomRight)
               && HasVerticalEdge(diagram, upperLeft, bottomLeft)
               && HasVerticalEdge(diagram, upperRight, bottomRight);
    }

    private static bool HasHorizontalEdge(IReadOnlyList<string> diagram, Coordinate leftCoordinate, Coordinate rightCoordinate)
    {
        if (leftCoordinate.Row != rightCoordinate.Row)
        {
            throw new InvalidOperationException(
                $"Left {leftCoordinate} and Right {rightCoordinate} are not on the same row");
        }

        string row = diagram[leftCoordinate.Row];
        int startColumn = leftCoordinate.Column;
        int endColumn = rightCoordinate.Column;
        return Enumerable.Range(startColumn + 1, endColumn - startColumn - 1)
            .All(column => row[column] == '+' || row[column] == '-');
    }

    
    private static bool HasVerticalEdge(IReadOnlyList<string> diagram, Coordinate upperCoordinate, Coordinate lowerCoordinate)
    {
        if (upperCoordinate.Column != lowerCoordinate.Column)
        {
            throw new InvalidOperationException(
                $"Upper {upperCoordinate} and Lower {lowerCoordinate} are not in same column");
        }
        
        int column = upperCoordinate.Column;
        int startRow = upperCoordinate.Row;
        int endRow = lowerCoordinate.Row;
        return  Enumerable.Range(startRow + 1, endRow - startRow - 1)
            .All(row => diagram[row][column] == '+' || diagram[row][column] == '|');
    }
    
    private record Coordinate(int Row, int Column);
}