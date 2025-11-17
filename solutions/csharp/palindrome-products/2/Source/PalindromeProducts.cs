using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class PalindromeProducts
{
	public static (int, IEnumerable<(int, int)>) Largest(int minimumFactor, int maximumFactor)
		=> FindPalindrome(minimumFactor, maximumFactor, FindMaxPalindrome);

	public static (int, IEnumerable<(int, int)>) Smallest(int minimumFactor, int maximumFactor)
		=> FindPalindrome(minimumFactor, maximumFactor, FindMinPalindrome);

	private static (int, IEnumerable<(int, int)>) FindPalindrome(
		int minimumFactor, int maximumFactor, Func<int, int, int> palindromeFinder
	)
	{
		ValidateInput(minimumFactor, maximumFactor);
		int palindrome = palindromeFinder(minimumFactor, maximumFactor);
		return CalculateFactors(minimumFactor, maximumFactor, palindrome);
	}

	private static void ValidateInput(int minimumFactor, int maximumFactor)
	{
		if (minimumFactor > maximumFactor)
		{
			throw new ArgumentException("minFactor cannot be greater than maxFactor.");
		}
	}

	private static int FindMaxPalindrome(int minimumFactor, int maximumFactor)
	{
		int maxPalindrome = int.MinValue;
		for (int greaterFactor = maximumFactor; greaterFactor >= minimumFactor; greaterFactor--)
		{
			maxPalindrome = FindLesserFactor(minimumFactor, maxPalindrome, greaterFactor);
		}

		return maxPalindrome == int.MinValue
			? throw new ArgumentException("No palindromes found in the given range.")
			: maxPalindrome;
	}

	private static int FindLesserFactor(int minFactor, int maxPalindrome, int greaterFactor)
	{
		for (int lesserFactor = greaterFactor; lesserFactor >= minFactor; lesserFactor--)
		{
			int product = greaterFactor * lesserFactor;
			if (product <= maxPalindrome)
			{
				return maxPalindrome;
			}

			if (IsPalindrome(product) &&
				product > maxPalindrome)
			{
				maxPalindrome = product;
			}
		}

		return maxPalindrome;
	}

	private static int FindMinPalindrome(int minimumFactor, int maximumFactor)
	{
		int minPalindrome = int.MaxValue;
		for (int i = minimumFactor; i <= maximumFactor; i++)
		{
			for (int j = i; j <= maximumFactor; j++)
			{
				int product = i * j;
				if (product >= minPalindrome)
				{
					return minPalindrome;
				}

				if (IsPalindrome(product))
				{
					minPalindrome = product;
				}
			}
		}

		throw new ArgumentException("No palindromes found in the given range.");
	}

	private static bool IsPalindrome(int number)
	{
		string numberStr = number.ToString(CultureInfo.InvariantCulture);
		return numberStr.SequenceEqual(numberStr.Reverse());
	}

	private static (int, IEnumerable<(int, int)>) CalculateFactors(int minimumFactor, int maximumFactor, int product)
	{
		List<(int, int)> factors = new();
		for (int i = minimumFactor; i <= Math.Sqrt(product); i++)
		{
			if (product % i == 0)
			{
				int j = product / i;
				if (IsInBounds(minimumFactor, maximumFactor, i) &&
					IsInBounds(minimumFactor, maximumFactor, j))
				{
					factors.Add((i, j));
				}
			}
		}

		return (product, factors);
	}

	private static bool IsInBounds(int minimumFactor, int maximumFactor, int value)
		=> value >= minimumFactor &&
		   value <= maximumFactor;
}