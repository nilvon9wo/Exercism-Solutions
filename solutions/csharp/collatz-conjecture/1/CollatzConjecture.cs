using System;

public static class CollatzConjecture
{
    public static int Steps(int number) =>
        number <= 0
            ? throw new ArgumentOutOfRangeException("Number must be non-negative")
            : number == 1
                ? 0
                : number % 2 == 0
                    ? Steps(number / 2) + 1
                    : Steps(3 * number + 1) + 1;
}