using System;
using System.Collections.Generic;
using System.Linq;

public static class Change
{
	public static int[] FindFewestCoins(int[] denominations, int target)
	{
		if (target == 0)
		{
			return Array.Empty<int>();
		}

		if (target < denominations.Min())
		{
			throw new ArgumentException(nameof(target), $"No denominations are small enough to return {target}.");
		}

		List<int> consumedCoins = CreateCombinations(denominations, target)
			.OrderBy(x => x.Count)
			.FirstOrDefault();

		return (consumedCoins == default)
			? throw new ArgumentException($"Target {target} is not reachable with coins {denominations}.", nameof(target))
			: consumedCoins
				.OrderBy(x => x)
				.ToArray();
	}

	private static List<List<int>> CreateCombinations(int[] denominations, int target)
	{
		int[] orderedDenominations = denominations
			.OrderByDescending(x => x)
			.Where(x => x <= target)
			.ToArray();

		List<List<int>> combinations = new();
		foreach (int denomination in orderedDenominations)
		{
			int maxMultiple = target / denomination;
			for (int multiple = maxMultiple; multiple > 0; multiple--)
			{
				List<int> change = CreateChange(denomination, multiple);
				int remainder = target - change.Sum();

				if (remainder != 0)
				{
					int[] remainingDenominations = orderedDenominations.Where(x => x < denomination && x <= remainder)
						.ToArray();
					if (remainingDenominations.Contains(remainder))
					{
						change.Add(remainder);
					}
					else
					{
						try
						{
							int[] remainderChange = FindFewestCoins(remainingDenominations, remainder);
							change.AddRange(remainderChange);
						}
						catch (Exception)
						{
							// Ignore failures
						}
					}
				}

				if (change.Sum() == target)
				{
					combinations.Add(change);
				}
			}
		}

		return combinations;
	}

	private static List<int> CreateChange(int denomination, int quantity)
	{
		List<int> result = new();
		for (int i = 1; i <= quantity; i++)
		{
			result.Add(denomination);
		}

		return result;
	}
}
