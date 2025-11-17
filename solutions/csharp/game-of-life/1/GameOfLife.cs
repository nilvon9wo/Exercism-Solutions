#pragma warning disable CA1050
#pragma warning disable IDE0130

public static class GameOfLife
{
    public static int[,] Tick(int[,] matrix)
        => Matrix.From(matrix)
            .ToNextState()
            .ToIntArray();
}

#pragma warning disable CA1050
#pragma warning disable IDE0130

internal class Matrix(Dictionary<Coordinate, bool> hasLifeByCoordinates)
{
    private const int _hasLife = 1;
    private const int _isLifeless = 0;
    private static readonly IEnumerable<int> _neighborOffsets = Enumerable.Range(-1, 3);

    internal Matrix ToNextState()
    {
        HashSet<Coordinate> coordinatesToCheck = this.GetAllCoordinatesToCheck();

        Dictionary<Coordinate, bool> nextStateLifeByCoordinates = coordinatesToCheck
            .ToDictionary(coordinate => coordinate, this.WillHaveLifeInNextState);

        return new(nextStateLifeByCoordinates);
    }

    private bool WillHaveLifeInNextState(Coordinate coordinate)
    {
        bool currentlyHasLife = hasLifeByCoordinates.GetValueOrDefault(coordinate, false);
        int liveNeighborCount = this.CountLiveNeighbors(coordinate);

        bool liveCellSurvives = currentlyHasLife
                                && liveNeighborCount is 2 or 3;
        bool deadCellBecomesAlive = !currentlyHasLife
                                    && liveNeighborCount == 3;

        return liveCellSurvives || deadCellBecomesAlive;
    }

    private int CountLiveNeighbors(Coordinate coordinate)
        => GetNeighborCoordinates(coordinate)
            .Count(neighbor => hasLifeByCoordinates.GetValueOrDefault(neighbor, false));

    private static IEnumerable<Coordinate> GetNeighborCoordinates(Coordinate coordinate)
        => _neighborOffsets
            .SelectMany(dx => _neighborOffsets
                .Select(dy => new { dx, dy }))
            .Where(offset => offset.dx != 0 || offset.dy != 0)
            .Select(offset => new Coordinate(coordinate.X + offset.dx, coordinate.Y + offset.dy));

    private HashSet<Coordinate> GetAllCoordinatesToCheck()
    {
        (int maxRow, int maxCol)? originalBounds = this.GetOriginalMatrixBounds();
        if (originalBounds == null)
        {
            return [];
        }

        (int maxRow, int maxCol) = originalBounds.Value;
        IEnumerable<Coordinate> liveCells = hasLifeByCoordinates.Keys
            .Where(coordinate => hasLifeByCoordinates[coordinate]);
        return [.. GetRelevantCoordinatesForLiveCells(liveCells, maxRow, maxCol)];
    }

    private (int maxRow, int maxCol)? GetOriginalMatrixBounds()
    {
        IEnumerable<Coordinate> originalCoordinates = hasLifeByCoordinates.Keys
            .Where(IsValidOriginalCoordinate);
        IEnumerable<Coordinate> coordinates = originalCoordinates as Coordinate[] ?? originalCoordinates.ToArray();
        return coordinates.Any()
            ? (
                coordinates.Max(c => c.X),
                coordinates.Max(c => c.Y)
                )
            : null;
    }

    private static IEnumerable<Coordinate> GetRelevantCoordinatesForLiveCells(
            IEnumerable<Coordinate> liveCells,
            int maxRow,
            int maxCol
        )
        => liveCells.SelectMany(liveCell => GetValidNeighborsAndSelf(liveCell, maxRow, maxCol));

    private static IEnumerable<Coordinate> GetValidNeighborsAndSelf(Coordinate coordinate, int maxRow, int maxCol)
        => GetNeighborCoordinates(coordinate)
            .Where(neighbor => IsWithinBounds(neighbor, maxRow, maxCol))
            .Append(coordinate);

    private static bool IsWithinBounds(Coordinate coordinate, int maxRow, int maxCol)
        => coordinate is { X: >= 0, Y: >= 0 }
           && coordinate.X <= maxRow
           && coordinate.Y <= maxCol;

    internal static Matrix From(int[,]? matrix)
    {
        matrix ??= new int[0, 0];
        IEnumerable<Coordinate> coordinates = GenerateAllCoordinates(matrix);
        Dictionary<Coordinate, bool> hasLifeByCoordinates = coordinates.ToDictionary(
            coordinate => coordinate,
            coordinate => CellHasLife(matrix, coordinate)
        );

        return new(hasLifeByCoordinates);
    }

    private static bool CellHasLife(int[,] matrix, Coordinate coordinate)
        => matrix[coordinate.X, coordinate.Y] == _hasLife;

    internal int[,] ToIntArray()
    {
        if (hasLifeByCoordinates.Count == 0)
        {
            return new int[0, 0];
        }

        IEnumerable<Coordinate> originalCoordinates = hasLifeByCoordinates.Keys.Where(IsValidOriginalCoordinate);
        IEnumerable<Coordinate> coordinates = originalCoordinates as Coordinate[] ?? originalCoordinates.ToArray();
        int maxX = coordinates.Max(coordinate => coordinate.X);
        int maxY = coordinates.Max(coordinate => coordinate.Y);
        return GenerateAllCoordinates(new int[maxX + 1, maxY + 1])
            .Aggregate(
                new int[maxX + 1, maxY + 1],
                (matrix, coordinate) => {
                    matrix[coordinate.X, coordinate.Y] = this.ToInteger(coordinate);
                    return matrix;
                });
    }

    private int ToInteger(Coordinate coordinate)
        => hasLifeByCoordinates.GetValueOrDefault(coordinate, false)
            ? _hasLife
            : _isLifeless;

    private static IEnumerable<Coordinate> GenerateAllCoordinates(int[,] matrix)
        => Enumerable.Range(0, matrix.GetLength(0))
            .SelectMany(row => Enumerable.Range(0, matrix.GetLength(1))
                .Select(column => new Coordinate(row, column)));

    private static bool IsValidOriginalCoordinate(Coordinate coordinate)
        => coordinate is { X: >= 0, Y: >= 0 };
}

#pragma warning disable CA1050
#pragma warning disable IDE0079
#pragma warning disable IDE0130
internal class Coordinate(int x, int y)
{
    public int X { get; init; } = x;
    public int Y { get; init; } = y;

    public override bool Equals(object? value)
        => value is Coordinate coordinate
           && this.Equals(coordinate);

    protected bool Equals(Coordinate other)
        => this.X == other.X
           && this.Y == other.Y;

    public override int GetHashCode()
        => HashCode.Combine(this.X, this.Y);

    public override string ToString()
        => $"({this.X}, {this.Y})";
}