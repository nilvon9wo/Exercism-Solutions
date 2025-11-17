using System;
using System.Linq;

public static class Grains
{
    public static ulong Square(int n) => 
        n < 1 || n > 64
            ? throw new ArgumentOutOfRangeException("The requested square must be between 1 and 64 (inclusive)")
            : Convert.ToUInt64(Math.Pow(2, n - 1));

    public static ulong Total() => 
        Enumerable.Range(1, 64)
            .Select(Square).Aggregate((accumulator, value) => accumulator + value);
}