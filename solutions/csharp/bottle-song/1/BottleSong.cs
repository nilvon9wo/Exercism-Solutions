using System.Collections.Generic;
using System.Linq;

#pragma warning disable CA1050
#pragma warning disable IDE0079
#pragma warning disable IDE0130
public static class BottleSong
#pragma warning restore IDE0130
{
    private const string Separator = "\n";

    public static IEnumerable<string> Recite(int startBottles, int takeDown)
    {
        var allVerses = Enumerable.Range(0, takeDown)
            .Select(i => GetVerse(startBottles - i))
            .Select(verse => string.Join(Separator, verse));

        return string.Join(Separator + Separator, allVerses)
            .Split(Separator.ToCharArray());
    }

    private static readonly Dictionary<int, string> NumberToWords = new()
    {
        { 0, "no" },
        { 1, "One" },
        { 2, "Two" },
        { 3, "Three" },
        { 4, "Four" },
        { 5, "Five" },
        { 6, "Six" },
        { 7, "Seven" },
        { 8, "Eight" },
        { 9, "Nine" },
        { 10, "Ten" }
    };

    private static string GetVerse(int thisNumber)
    {
        string thisNumberWord = NumberToWords[thisNumber];
        string thisBottleWord = GetBottleWord(thisNumber);

        var nextNumber = thisNumber - 1;
        string nextNumberWord = NumberToWords[nextNumber];
        string nextBottleWord = GetBottleWord(nextNumber);

        return $"""
            {thisNumberWord} green {thisBottleWord} hanging on the wall,
            {thisNumberWord} green {thisBottleWord} hanging on the wall,
            And if one green bottle should accidentally fall,
            There'll be {nextNumberWord.ToLowerInvariant()} green {nextBottleWord} hanging on the wall.
            """;
    }

    private static string GetBottleWord(int number) =>
        number == 1 
            ? "bottle" 
            : "bottles";
}
