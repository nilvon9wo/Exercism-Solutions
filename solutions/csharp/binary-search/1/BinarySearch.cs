using System;

public static class BinarySearch
{
    public static int Find(int[] input, int targetValue)
    {
        var index = Array.BinarySearch(input, targetValue);
        return (index >= 0)
            ? index
            : -1;
    }
}