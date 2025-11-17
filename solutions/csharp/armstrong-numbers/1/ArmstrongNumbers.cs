using System;
using System.Linq;

public static class ArmstrongNumbers
{
    public static bool IsArmstrongNumber(int number) =>
        number == SumOfDigitsToPowerOfLength(number);

    private static double SumOfDigitsToPowerOfLength(int number)
    {
        string numberString = number.ToString();
        return numberString.ToCharArray()
            .ToList()
            .Select(x => SumOfDigitsToPowerOfLength(x, numberString.Count()))
            .Sum();
    }

    private static double SumOfDigitsToPowerOfLength(char x, int power) =>
        Math.Pow(int.Parse(x.ToString()), power);
}