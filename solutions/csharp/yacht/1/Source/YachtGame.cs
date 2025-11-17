using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class YachtGame
{
	private const int _straightPoints = 30;
	private const int _allSameFacePoints = 50;

	private static readonly Dictionary<YachtCategory, Func<int[], int>> _categoryScorerByCategory = new()
	{
		{ YachtCategory.Ones, CreateCategoryScorer(1) },
		{ YachtCategory.Twos, CreateCategoryScorer(2) },
		{ YachtCategory.Threes, CreateCategoryScorer(3) },
		{ YachtCategory.Fours, CreateCategoryScorer(4) },
		{ YachtCategory.Fives, CreateCategoryScorer(5) },
		{ YachtCategory.Sixes, CreateCategoryScorer(6) },
		{ YachtCategory.FullHouse, FullHouseScore },
		{ YachtCategory.FourOfAKind, FourOfAKindScore },
		{ YachtCategory.LittleStraight, LittleStraightScore },
		{ YachtCategory.BigStraight, BigStraightScore },
		{ YachtCategory.Choice, d => d.Sum() },
		{ YachtCategory.Yacht, YachtScore },
	};

	private static Func<int[], int> CreateCategoryScorer(int targetNumber)
		=> die => die.Count(num => num == targetNumber) * targetNumber;

	public static int Score(int[] dice, YachtCategory category)
		=> _categoryScorerByCategory.TryGetValue(category, out Func<int[], int>? scorer)
			? scorer(dice)
			: throw new ArgumentOutOfRangeException(nameof(category));

	private static int FullHouseScore(int[] dice)
		=> HasTwoOrThreeDistinctGroups(dice)
			? dice.Sum()
			: 0;

	private static bool HasTwoOrThreeDistinctGroups(IEnumerable<int> dice)
	{
		List<IGrouping<int, int>> groups = dice.GroupBy(d => d)
			.ToList();
		return (groups.Count == 2) && groups.All(g => g.Count() is 2 or 3);
	}

	private static int FourOfAKindScore(int[] dice)
	{
		IGrouping<int, int>? fourOfAKindGroup = dice.GroupBy(d => d)
			.FirstOrDefault(g => g.Count() >= 4);
		return (fourOfAKindGroup?.Key ?? 0) * 4;
	}

	private static int LittleStraightScore(int[] dice)
		=> IsDistinctFiveValues(dice) &&
		   dice.All(d => d is >= 1 and <= 5)
			? _straightPoints
			: 0;

	private static int BigStraightScore(int[] dice)
		=> IsDistinctFiveValues(dice) &&
		   dice.All(d => d is >= 2 and <= 6)
			? _straightPoints
			: 0;

	private static bool IsDistinctFiveValues(IEnumerable<int> dice)
		=> dice.Distinct()
			   .Count() ==
		   5;

	private static int YachtScore(int[] dice)
		=> dice.All(d => d == dice[0])
			? _allSameFacePoints
			: 0;
}