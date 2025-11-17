using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class NthPrime
{
	public static int Prime(int nth)
		=> nth < 1
			? throw new ArgumentOutOfRangeException(
				nameof(nth),
				$"The input must be greater than or equal to 1. Received: {nth}"
			)
			: EnumeratePrimes()
				.Skip(nth - 1)
				.First();

	private static IEnumerable<int> EnumeratePrimes()
	{
		yield return 2;
		for (int i = 3; ; i += 2)
		{
			if (IsPrime(i))
			{
				yield return i;
			}
		}

		// ReSharper disable once IteratorNeverReturns
	}

	private static bool IsPrime(int n)
	{
		if (n.IsEven())
		{
			return false;
		}

		for (int i = 3; i <= Math.Sqrt(n); i += 2)
		{
			if (n.HasFactor(i))
			{
				return false;
			}
		}

		return true;
	}

	private static bool HasFactor(this int n, int i)
		=> (n % i) == 0;

	private static bool IsEven(this int n)
		=> (n % 2) == 0;
}