using System.Collections.Generic;
using System.Linq;

public static class PrimeFactors
{
    public static long[] Factors(long number) =>
        FactorsFor(number)
        .ToArray();

    public static IEnumerable<long> FactorsFor(long i)
    {
        int factor = 2;
        while (i > 1)
        {
            while (i % factor != 0) {
                factor++;
            }

            i /= factor;
            yield return factor;
        }
    }
}