using System;
using System.Collections.Generic;
using System.Linq;

public enum Classification
{
    Perfect,
    Abundant,
    Deficient
}

public static class PerfectNumbers
{
    public static Classification Classify(int number) => 
        number < 1
            ? throw new ArgumentOutOfRangeException("Classification is only possible for natural numbers.")
            : (number == 1)
                ? Classification.Deficient
                : FindNumberType(number);

    private static Classification FindNumberType(int number)
    {
        int factorSum = FindFactors(number)
            .Sum();

        if (factorSum < number)
        {
            return Classification.Deficient;
        }
        else if (factorSum == number)
        {
            return Classification.Perfect;
        }
        else if (factorSum > number) 
        {
            return Classification.Abundant;
        }
        
        throw new ArgumentOutOfRangeException("Something went wrong");
    }

    private static List<int> FindFactors(int number) => 
        Enumerable.Range(1, number - 1)
            .Where(candidate => number % candidate == 0)
            .ToList();
}
