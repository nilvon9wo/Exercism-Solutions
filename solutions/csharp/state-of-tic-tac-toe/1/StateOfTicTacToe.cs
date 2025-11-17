#pragma warning disable CA1050
#pragma warning disable IDE0130

using System.Reflection;

public class TicTacToe(IEnumerable<string> rows)
{
    private static readonly int[] _boardIndices = [0, 1, 2];
    private static readonly Coordinate[] _fallingDiagonal = [new(0, 0), new(1, 1), new(2, 2)];
    private static readonly Coordinate[] _risingDiagonal = [new(2, 0), new(1, 1), new(0, 2)];

    private TitTacToeBoard Board
        => TitTacToeBoard.From(rows);

    public State State
        => this.Evaluate();

    private State Evaluate()
    {
        (int xCount, int oCount) = this.CountOccupants();
        bool hasWin = this.HasWin();

        return this.IsInvalid(xCount, oCount, hasWin)
            ? State.Invalid
            : hasWin
                ? State.Win
                : HasDraw(xCount, oCount)
                    ? State.Draw
                    : State.Ongoing;
    }

    private bool IsInvalid(int xCount, int oCount, bool hasWin)
        => HasInvalidTurnOrder(xCount, oCount) ||
           this.HasInvalidWinState(xCount, oCount, hasWin);

    private bool HasInvalidWinState(int xCount, int oCount, bool hasWin)
        => hasWin
           && (this.HasInvalidWinnerTurnCount(xCount, oCount)
               || HasInsufficientMovesForWin(xCount, oCount));

    private static bool HasDraw(int xCount, int oCount)
        => xCount + oCount == 9;

    private static bool HasInvalidTurnOrder(int xCount, int oCount)
        => xCount < oCount
           || xCount > oCount + 1;

    private bool HasInvalidWinnerTurnCount(int xCount, int oCount)
        => (this.HasWinningOccupant(Occupant.X) && xCount != oCount + 1)
           || (this.HasWinningOccupant(Occupant.O) && xCount != oCount);

    private static bool HasInsufficientMovesForWin(int xCount, int oCount)
        => xCount + oCount < 5;

    private (int XCount, int OCount) CountOccupants()
    {
        Dictionary<Occupant, int> occupants = _boardIndices
            .SelectMany(x => _boardIndices.Select(y => this.Board.GetOccupant(new(x, y))))
            .Where(static occupant => occupant != Occupant.None)
            .GroupBy(static occupant => occupant)
            .ToDictionary(static group => group.Key, static group => group.Count());

        return (occupants.GetValueOrDefault(Occupant.X, 0),
            occupants.GetValueOrDefault(Occupant.O, 0));
    }

    private bool HasWinningOccupant(Occupant targetOccupant)
        => this.HasLineWinFor(targetOccupant, static (x, y) => new(x, y))
           || this.HasLineWinFor(targetOccupant, static (x, y) => new(y, x))
           || this.HasDiagonalWinFor(targetOccupant, _fallingDiagonal)
           || this.HasDiagonalWinFor(targetOccupant, _risingDiagonal);

    private bool HasLineWinFor(Occupant targetOccupant, Func<int, int, Coordinate> coordinateMapper)
        => _boardIndices
            .Any(lineIndex =>
                _boardIndices.All(positionIndex =>
                    this.Board.GetOccupant(coordinateMapper(lineIndex, positionIndex)) == targetOccupant));

    private bool HasDiagonalWinFor(Occupant targetOccupant, IEnumerable<Coordinate> coordinates)
        => coordinates.All(coordinate => this.Board.GetOccupant(coordinate) == targetOccupant);

    private bool HasWin()
        => this.HasHorizontalWin()
           || this.HasVerticalWin()
           || this.HasDiagonalWin();

    private bool HasVerticalWin()
        => this.HasLineWin(static (x, y) => new(x, y));

    private bool HasHorizontalWin()
        => this.HasLineWin(static (x, y) => new(y, x));

    private bool HasLineWin(Func<int, int, Coordinate> coordinateMapper)
        => _boardIndices
            .Any(lineIndex =>
            {
                Occupant occupant = this.Board.GetOccupant(coordinateMapper(lineIndex, 0));
                return occupant != Occupant.None &&
                       _boardIndices.All(positionIndex =>
                           this.Board.GetOccupant(coordinateMapper(lineIndex, positionIndex)) == occupant);
            });

    private bool HasDiagonalWin()
        => this.HasDiagonalWin(_fallingDiagonal)
           || this.HasDiagonalWin(_risingDiagonal);

#pragma warning disable CA1859
    private bool HasDiagonalWin(IReadOnlyList<Coordinate> coordinates)
#pragma warning restore CA1859
    {
        Occupant occupant = this.Board.GetOccupant(coordinates[0]);
        return occupant != Occupant.None &&
               coordinates.All(coordinate => this.Board.GetOccupant(coordinate) == occupant);
    }
}

#pragma warning disable CA1050
#pragma warning disable IDE0130
public enum State
{
    Win,
    Draw,
    Ongoing,
    Invalid
}

#pragma warning disable CA1050
#pragma warning disable IDE0130
public class TitTacToeBoard(IReadOnlyDictionary<Coordinate, Occupant> occupantByPositions)
{
    public static TitTacToeBoard From(IEnumerable<string>? rows)
    {
        rows ??= [];
        return new(
            rows.SelectMany(CollectionSelector)
                .ToDictionary(static kvp => kvp.Key, static kvp => kvp.Value)
        );
    }

    public Occupant GetOccupant(Coordinate coordinate)
        => occupantByPositions.GetValueOrDefault(coordinate, Occupant.None);

    private static IEnumerable<KeyValuePair<Coordinate, Occupant>> CollectionSelector(string row, int y)
        => row.Select((cell, x)
            => new KeyValuePair<Coordinate, Occupant>(
                new(x, y),
                OccupantMapper.FromCharacter(cell))
        );
}

#pragma warning disable CA1050
#pragma warning disable IDE0130
public class Coordinate(int x, int y)
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

#pragma warning disable CA1050
#pragma warning disable IDE0130

[AttributeUsage(AttributeTargets.Field)]
public class CharacterAttribute(char character) : Attribute
{
    public char Character { get; } = character;
}

#pragma warning disable CA1050
#pragma warning disable IDE0130
public readonly struct CharacterOccupantMapping(char character, Occupant occupant)
{
    public char Character { get; } = character;
    public Occupant Occupant { get; } = occupant;
}

#pragma warning disable CA1050
#pragma warning disable IDE0130
public enum Occupant
{
    [Character(' ')] None,
    [Character('X')] X,
    [Character('O')] O
}

#pragma warning disable CA1050
#pragma warning disable IDE0130

public static class OccupantMapper
{
    private static readonly Dictionary<char, Occupant> _characterToOccupant =
        Enum.GetValues<Occupant>()
            .Select(GetOccupantCharacterMapping)
            .Where(static mapping => mapping.HasValue)
            .ToDictionary(
                // ReSharper disable NullableWarningSuppressionIsUsed
                static mapping => mapping!.Value.Character,
                static mapping => mapping!.Value.Occupant
                // ReSharper restore NullableWarningSuppressionIsUsed
            );

    public static Occupant FromCharacter(char character)
        => _characterToOccupant.GetValueOrDefault(character, Occupant.None);

    private static CharacterOccupantMapping? GetOccupantCharacterMapping(Occupant occupant)
    {
        CharacterAttribute? attribute = occupant.GetType()
            .GetField(occupant.ToString())
            ?.GetCustomAttribute<CharacterAttribute>();

        return attribute != null
            ? new CharacterOccupantMapping(attribute.Character, occupant)
            : null;
    }
}