#pragma warning disable CA1050
#pragma warning disable IDE0079
#pragma warning disable IDE0130
public static class FlowerField
{
    private const int AdjacentOffset = -1;
    private static readonly int[] AdjacentOffsets = [AdjacentOffset, 0, 1];
    private const char EmptyCharacter = ' ';
    private const char FlowerCharacter = '*';

    public static string[] Annotate(string[] input)
    {
        input = input ?? throw new ArgumentNullException(nameof(input));
        switch (input.Length)
        {
            case 0:
                return [];
            case 1 when input[0].Length == 0:
                return [""];
        }

        HashSet<Coordinate> flowerCoordinates = [.. input.SelectMany(FindFlowers)];
        Dictionary<Coordinate, char> charactersByCoordinates = MapCharactersByCoordinates(input, flowerCoordinates);
        return BuildOutput(input, charactersByCoordinates);
    }

    private static Dictionary<Coordinate, char> MapCharactersByCoordinates(string[] input, HashSet<Coordinate> flowerCoordinates)
        => GetAllCoordinates(input)
            .ToDictionary(
                coordinate => coordinate,
                coordinate => GetCellCharacter(coordinate, flowerCoordinates));

    private static IEnumerable<Coordinate> GetAllCoordinates(string[] input)
        => input.SelectMany((row, y) => row
            .Select((_, x) => new Coordinate(x, y)));

    private static char GetCellCharacter(Coordinate coordinate, HashSet<Coordinate> flowerCoordinates)
    {
        if (flowerCoordinates.Contains(coordinate))
        {
            return FlowerCharacter;
        }

        int adjacentFlowers = CountAdjacentFlowers(coordinate, flowerCoordinates);
        return adjacentFlowers == 0
            ? EmptyCharacter
            : adjacentFlowers.ToString()[0];
    }

    private static IEnumerable<Coordinate> FindFlowers(string row, int y)
        => row.Select((character, x) => new { character, x })
            .Where(item => item.character == FlowerCharacter)
            .Select(item => new Coordinate(item.x, y));

    private static int CountAdjacentFlowers(Coordinate coordinate, HashSet<Coordinate> flowerCoordinates)
        => GetAdjacentCoordinates(coordinate)
            .Count(flowerCoordinates.Contains);

    private static IEnumerable<Coordinate> GetAdjacentCoordinates(Coordinate coordinate)
        => AdjacentOffsets
            .SelectMany(dy => AdjacentOffsets.Select(dx => new { dx, dy }))
            .Where(offset => !(offset.dx == 0 && offset.dy == 0))
            .Select(offset => new Coordinate(coordinate.X + offset.dx, coordinate.Y + offset.dy));

    private static string[] BuildOutput(string[] input, Dictionary<Coordinate, char> cellChars)
        => [.. input.Select((row, y) => 
            BuildRow(row, y, cellChars))
        ];

    private static string BuildRow(string row, int y, Dictionary<Coordinate, char> cellChars)
        => new([..
            row.Select((_, x) => cellChars[new Coordinate(x, y)])
        ]);
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
           && Equals(coordinate);

    protected bool Equals(Coordinate other)
        => X == other.X
           && Y == other.Y;

    public override int GetHashCode()
        => HashCode.Combine(X, Y);

    public override string ToString()
        => $"({X}, {Y})";
}