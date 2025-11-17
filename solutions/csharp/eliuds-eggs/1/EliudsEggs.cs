using System.Collections;

#pragma warning disable IDE0079
#pragma warning disable CA1050
#pragma warning disable IDE0130
public static class EliudsEggs
{
    public static int EggCount(int encodedCount) 
        => encodedCount.ToBitArray()
            .CountOnes();
}

internal static class IntegerExtensions
{
    internal static BitArray ToBitArray(this int value)
        => new([value]);
}

internal static class BitArrayExtensions
{
    internal static int CountOnes(this BitArray value)
        => value.Cast<bool>()
            .Count(bit => bit);
}
