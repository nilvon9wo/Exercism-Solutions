using System;
using System.Linq;

public static class Series
{
    public static string[] Slices(string numbers, int sliceLength) => 
        sliceLength <= 0
            ? throw new ArgumentException("Slice length must be greater than zero.")
            : numbers.Length < sliceLength
                ? throw new ArgumentException("Slice length must less than numbers length.")
                : Enumerable.Range(0, numbers.Length - sliceLength + 1)
                    .ToList()
                    .Select(part => numbers.Substring(part, sliceLength))
                    .ToArray();
}