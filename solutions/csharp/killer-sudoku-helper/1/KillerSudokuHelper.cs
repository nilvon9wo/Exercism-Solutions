#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public static class KillerSudokuHelper
{
    private static readonly int[] SingleDigits = [.. Enumerable.Range(1, 9)];

    public static IEnumerable<int[]> Combinations(int sum, int size, int[] exclude)
        => GenerateCombinations(SingleDigits, new(size, exclude))
            .Where(combination => combination.Sum() == sum);

    private static IEnumerable<int[]> GenerateCombinations(int[] availableDigits, KillerSudokuCage cage)
        => cage.Size == 0
            ? [[]]
            : availableDigits
                .Exclude(cage.Exclude)
                .Select((digit, index) => (digit, index))
                .SelectMany(item => SelectValidCombinations(availableDigits, cage, item));

    private static IEnumerable<int[]> SelectValidCombinations(
        int[] availableDigits,
        KillerSudokuCage cage,
        (int currentDigit, int index) item
    )
    {
        int[] digits = AvailableDigits(availableDigits, cage.Exclude, item.index);
        return GenerateCombinations(digits, cage.Shrink())
            .Select(combination
                => new[] { item.currentDigit }
                    .Concat(combination)
                    .ToArray()
            );
    }

    private static int[] AvailableDigits(int[] availableDigits, int[] exclude, int index) 
        => [.. 
            availableDigits.Exclude(exclude)
                .Skip(index + 1)
        ];

    private static IEnumerable<int> Exclude(this int[] availableDigits, int[] exclude)
        => availableDigits.Where(digit => !exclude.Contains(digit));
}

internal readonly record struct KillerSudokuCage(
    int Size,
    int[] Exclude
)
{
    internal KillerSudokuCage Shrink()
        => this with { Size = Size - 1 };
}