using System;
using System.Collections.Generic;
using System.Linq;

public static class Sieve
{
	public static int[] Primes(int limit) =>
		limit < 0
			? throw new ArgumentOutOfRangeException(nameof(limit), "Only positive numbers can be collected")
			: limit < 2
				? Array.Empty<int>()
				: CreateRange(2, limit)
					.ToDictionary(x => x, x => Classification.Unknown)
					.ClassifyNumbers()
					.ExtractPrimes()
					.ToArray();

	private static Dictionary<int, Classification> ClassifyNumbers(this Dictionary<int, Classification> classificationByValues)
	{
		int upperLimit = classificationByValues.ExtractHighestUnknown();
		while (classificationByValues.HasAnyUnknown())
		{
			int prime = classificationByValues.ExtractLowestUnknown();
			classificationByValues[prime] = Classification.Prime;

			for (int i = 2; i <= upperLimit; i++)
			{
				classificationByValues[prime * i] = Classification.Composite;
			}
		}

		return classificationByValues;
	}

	private static IEnumerable<int> CreateRange(int lowerLimit, int upperLimit) =>
		Enumerable.Range(lowerLimit, upperLimit - lowerLimit + 1);

	private static int ExtractLowestUnknown(this Dictionary<int, Classification> classificationByValues) =>
		classificationByValues.ExtractUnknown()
			.Min();

	private static int ExtractHighestUnknown(this Dictionary<int, Classification> classificationByValues) =>
		classificationByValues.ExtractUnknown()
			.Max();

	private static IEnumerable<int> ExtractUnknown(this Dictionary<int, Classification> classificationByValues) =>
		classificationByValues.Extract(Classification.Unknown);

	private static IEnumerable<int> ExtractPrimes(this Dictionary<int, Classification> classificationByValues) =>
		classificationByValues.Extract(Classification.Prime);

	private static IEnumerable<int> Extract(
			this Dictionary<int, Classification> classificationByValues,
			Classification targetClassification
		) =>
			classificationByValues.Where(keyValuePair =>
			{
				(int value, Classification classification) = keyValuePair;
				return classification == targetClassification;
			}).Select(keyValuePair =>
			{
				(int value, Classification classification) = keyValuePair;
				return value;
			});

	private static bool HasAnyUnknown(this Dictionary<int, Classification> classificationByValues) =>
		classificationByValues.Values.Contains(Classification.Unknown);

}

public enum Classification
{
	Prime,
	Composite,
	Unknown
}