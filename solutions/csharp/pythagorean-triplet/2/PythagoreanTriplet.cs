using System.Collections.Generic;
using System.Linq;

public static class PythagoreanTriplet
{
    public static IEnumerable<(int a, int b, int c)> TripletsWithSum(int sum) =>
        from a in Enumerable.Range(1, sum / 3)
        from b in Enumerable.Range(a + 1, sum / 2)
        where Square(a) + Square(b) == Square(sum - a - b)
        select (a, b, sum - a - b);

    private static int Square(int value) 
        => value * value;
}