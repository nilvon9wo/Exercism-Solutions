using System.Collections.Generic;

public static class PrimeFactors
{
    public static long[] Factors(long number) =>
        (number < 2)
            ? new long[] { }
            : Factors(number, 2, new List<long>());

    private static long[] Factors(long number, long nextAttempt, List<long> accumulator)
    {
        if (nextAttempt > number)
        {
            return accumulator.ToArray();
        }

        if (number % nextAttempt == 0)
        {
            accumulator.Add(nextAttempt);
            return Factors(number / nextAttempt, nextAttempt, accumulator);
        }
        else
        {
            return Factors(number, NextPrime(nextAttempt), accumulator);
        }
    }

    private static long NextPrime(long n) =>
        (n == 2)
            ? 3
            : n + 2;
}