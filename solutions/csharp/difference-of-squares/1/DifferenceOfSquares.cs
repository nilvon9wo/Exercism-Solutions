using System;

public static class DifferenceOfSquares
{
    public static int CalculateDifferenceOfSquares(int max) =>
        CalculateSquareOfSum(max) - CalculateSumOfSquares(max);

    public static int CalculateSumOfSquares(int max, int accumulated = 0) =>
        (max > 0)
            ? CalculateSumOfSquares(max - 1, accumulated + max * max)
            : accumulated;

    public static int CalculateSquareOfSum(int max, int accumulated = 0) =>
        (max > 0)
            ? CalculateSquareOfSum(max - 1, accumulated + max)
            : accumulated * accumulated;


}