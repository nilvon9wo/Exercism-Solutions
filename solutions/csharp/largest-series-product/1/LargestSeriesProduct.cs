using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

public static class LargestSeriesProduct
{
    public static long GetLargestProduct(string digits, int span) =>
        span == 0
            ? 1
            : IsInvalidInput(digits, span)
                ? throw new ArgumentException("Invalid Input")
                : FindMaxProduct(digits, span);

    private static bool IsInvalidInput(string digits, int span) =>
        span < 0
        || span > digits.Length
        || string.IsNullOrEmpty(digits)
        || !digits.ToCharArray()
            .ToList()
            .All(char.IsDigit);

    private static int FindMaxProduct(string digits, int span)
    {
        IEnumerable<int> startValues = (digits.Length - span == 0)
            ? new List<int> { 0 }
            : Enumerable.Range(0, digits.Length);

        return startValues
            .Select(start => ExtractSubseries(digits, span, start))
            .Distinct()
            .Select(substring => PairToProduct(substring))
            .ToDictionary(x => x.Item1, x => x.Item2)
            .Values.Max();
    }


    private static (string, int) PairToProduct(string substring)
    {
        if (string.IsNullOrEmpty(substring))
        {
            return (substring, -1);
        }

        int product = substring.ToCharArray()
            .Select(character => int.Parse(character.ToString()))
            .Aggregate((acc, x) => acc * x);

        return (substring, product);
    }

    private static string ExtractSubseries(string digits, int span, int start) =>
        (start + span <= digits.Length)
            ? digits.Substring(start, span)
            : "";


}