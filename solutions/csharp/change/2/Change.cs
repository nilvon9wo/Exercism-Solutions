using System;
using System.Collections.Generic;
using System.Linq;
public static class Change
{
	public static int[] FindFewestCoins(int[] denominations, int targetValue) =>
		targetValue switch
		{
			< 0 =>
				throw new ArgumentException("Change cannot be negative.", nameof(targetValue)),

			> 0 when targetValue < denominations.Min() =>
				throw new ArgumentException("Change cannot be less than minimal coin value.", nameof(targetValue)),

			_ =>
				Enumerable.Range(1, targetValue)
					.Aggregate(
					new Dictionary<int, int[]>
					{
						[0] = Array.Empty<int>()
					},
					denominations.MapFewestCoinsToValues
					)
					.GetValueOrDefault(targetValue)
						?? throw new ArgumentException($"No change is possible for target value {targetValue}", nameof(targetValue))
		};

	private static Dictionary<int, int[]> MapFewestCoinsToValues(
			this int[] denominations,
			Dictionary<int, int[]> coinsByTargetValue,
			int targetValue
		)
	{
		int[] fewestCoins = denominations.FindFewestCoins(targetValue, coinsByTargetValue);
		if (fewestCoins != null)
		{
			coinsByTargetValue.Add(targetValue, fewestCoins);
		}

		return coinsByTargetValue;
	}

	private static int[] FindFewestCoins(
			this int[] denominations,
			int targetValue,
			Dictionary<int, int[]> coinsByTargetValue
		) =>
		denominations
			.Where(denomination => denomination <= targetValue)
			.Select(value => coinsByTargetValue.GetValueOrDefault(targetValue - value)
				?.Prepend(value)
				.ToArray()
			)
			.Where(fewestCoins => fewestCoins != null)
			.OrderBy(fewestCoins => fewestCoins.Length)
			.FirstOrDefault();

}