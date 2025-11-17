using System.Collections.Generic;
using System.Linq;

public static class SumOfMultiples
{
    public static int Sum(IEnumerable<int> multiples, int max) =>
        multiples.Where(factor => factor != 0)
            .SelectMany(factor => FindMultiples(factor, max))        
            .Distinct()
            .Sum();

    private static IEnumerable<int> FindMultiples(int factor, int max) =>
        Enumerable.Range(1, (max - 1) / factor)
            .ToList()
            .Select(x => x * factor);
}