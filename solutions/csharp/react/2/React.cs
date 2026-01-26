using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace
public sealed class Reactor
{
    [SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by test.")]
    // ReSharper disable once MemberCanBeMadeStatic.Global
    public InputCell CreateInputCell(int value)
        => new(value);

    [SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by test.")]
    // ReSharper disable once MemberCanBeMadeStatic.Global
    public ComputeCell CreateComputeCell(IEnumerable<Cell> producers, Func<int[], int>? compute)
        => new(producers.ToArray(), compute);
}


