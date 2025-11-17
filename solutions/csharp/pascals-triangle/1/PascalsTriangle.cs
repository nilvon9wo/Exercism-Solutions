using System.Collections.Generic;
using System.Linq;

public static class PascalsTriangle
{
    public static IEnumerable<IEnumerable<int>> Calculate(int rows) =>
        (rows == 0)
            ? new List<List<int>>()
            : (rows == 1)
                ? new List<List<int>> {
                        new List<int> { 1 }
                    }
                : CalculateRows(rows, new List<List<int>> {
                    new List<int> { 1 },
                    new List<int>{ 1, 1 }
                });

    private static List<List<int>> CalculateRows(int rows, List<List<int>> accumulator)
    {
        if (rows == accumulator.Count)
        {
            return accumulator;
        }
        else
        {
            accumulator.Add(MakeNextRow(accumulator[^1], new List<int>{ 1 }));
            return CalculateRows(rows, accumulator);
        }
    }

    private static List<int> MakeNextRow(List<int> previousRow, List<int> accumulator)
    {
        int head = previousRow[0];
        if (previousRow.Count == 2)
        {
            accumulator.Add(head + 1);
            accumulator.Add(1);
            return accumulator;
        }
        else
        {
            List<int> clonedRow = previousRow.Select(x => x).ToList();
            var second = clonedRow[1];
            clonedRow.RemoveAt(0);
            accumulator.Add(head + second);
            return MakeNextRow(clonedRow, accumulator);
        }
    }
}