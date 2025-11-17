using System;
using System.Collections.Generic;
using System.Linq;

public static class AllYourBase
{
    public static int[] Rebase(int inputBase, int[] inputDigits, int outputBase) =>
        HasInvalidInput(inputBase, inputDigits, outputBase)
            ? throw new ArgumentException("Invalid Input")
            : Convert(inputBase, inputDigits.ToList(), outputBase);

    private static bool HasInvalidInput(int inputBase, int[] inputDigits, int outputBase) =>
        inputBase <= 1
            || outputBase <= 1
            || inputDigits.Any(x => x < 0)
            || inputDigits.Any(x => x >= inputBase);

    private static int[] Convert(int inputBase, List<int> inputDigits, int outputBase)
    {
        int firstCleanCharacterIndex = inputDigits.FindIndex(x => x != 0);
        if (firstCleanCharacterIndex == 1)
        {
            inputDigits.RemoveAt(0);
        }
        else if (firstCleanCharacterIndex > 1)
        {
            inputDigits.RemoveRange(0, firstCleanCharacterIndex - 1);
        }

        int base10 = RebaseToBase10(inputBase, inputDigits);
        return ReturnAsTargetBase(outputBase, base10);
    }

    private static int RebaseToBase10(int inputBase, List<int> inputDigits, int power = 0, int accumulated = 0) =>
        (power == inputDigits.Count)
            ? accumulated
            : RebaseToBase10(inputBase, inputDigits, power + 1, AddTranslatedValue(inputBase, inputDigits, power, accumulated));

    private static int AddTranslatedValue(int inputBase, List<int> inputDigits, int power, int accumulated)
    {
        int nextValuePosition = inputDigits.Count - power - 1;
        double multiplier = Math.Pow(inputBase, power);
        double newValue = inputDigits[nextValuePosition] * multiplier;
        return accumulated + (int)newValue;
    }

    private static int[] ReturnAsTargetBase(int outputBase, int base10) =>
        (base10 == 0)
            ? new int[] { 0 }
            : (outputBase == 10)
                ? ToIntList(base10)
                : RebaseToNewBase(outputBase, base10);

    private static int[] ToIntList(int base10) =>
        base10.ToString()
            .ToCharArray()
            .Select(x => int.Parse(x.ToString()))
            .ToArray();

    private static int[] RebaseToNewBase(int outputBase, int base10) =>
        RebaseToNewBase(outputBase, base10, HighestPowerOfBase(outputBase, base10), new List<int>());

    private static int[] RebaseToNewBase(int outputBase, int base10, int highestPower, List<int> accumulated) { 
        if (highestPower < 1)
        {
            return accumulated.ToArray();
        }
        else
        {
            accumulated.Add(base10 / highestPower);
            return RebaseToNewBase(outputBase, base10 % highestPower, highestPower / outputBase, accumulated);
        }
    }

    private static int HighestPowerOfBase(int outputBase, int base10, int lastAttempted = 1)
    {
        var nextAttempt = lastAttempted * outputBase;
        return (nextAttempt > base10)
            ? lastAttempted
            : HighestPowerOfBase(outputBase, base10, nextAttempt);
    }
}