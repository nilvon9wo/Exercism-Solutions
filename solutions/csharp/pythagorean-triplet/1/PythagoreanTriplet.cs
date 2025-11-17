using System;
using System.Collections.Generic;
using System.Linq;

public static class PythagoreanTriplet
{
    public static IEnumerable<(int a, int b, int c)> TripletsWithSum(int sum) =>
        GenerateTriplets(sum)
            .Where(triplet => triplet.a + triplet.b + triplet.c == sum);

    private static IEnumerable<(int a, int b, int c)> GenerateTriplets(int sum) =>
        GenerateTriplets(1, sum, new List<(int a, int b, int c)>());

    private static IEnumerable<(int a, int b, int c)> GenerateTriplets(int min, int max, List<(int a, int b, int c)> accumulator)
    {
        if (min > max)
        {
            return accumulator;
        }

        List<(int a, int b, int c)> triplets = Enumerable.Range(min, max)
            .Select(value => CalculateDistance(min, value))
            .Where(triplet => IsWholeNumber(triplet.c))
            .Select(triplet => (triplet.a, triplet.b, Convert.ToInt32(triplet.c)))
            .ToList();

        accumulator.AddRange(triplets);
        return GenerateTriplets(min + 1, max, accumulator);
    }

    private static bool IsWholeNumber(decimal value) =>
        value - Math.Round(value) == 0;

    private static (int a, int b, decimal c) CalculateDistance(int a, int b) =>
        (a, b, SquareRoot(Square(a) + Square(b)));

    private static int Square(int value) =>
        value * value;

    private static decimal SquareRoot(long value)
    {
        decimal sqrt_x = (decimal)Math.Sqrt(value);
        for (int i = 0; i < 10; ++i)
        {
            sqrt_x = 0.5m * (sqrt_x + value / sqrt_x);
        }
        return sqrt_x;
    }
}