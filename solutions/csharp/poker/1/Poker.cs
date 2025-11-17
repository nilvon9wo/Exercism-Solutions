using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Poker
{
	public static IEnumerable<string> BestHands(IEnumerable<string> handString)
		=> (handString ?? throw new ArgumentNullException(nameof(handString)))
			.Select(value => new HandInfo { HandString = value })
			.Aggregate(
				new BestHandInfo
				{
					BestHands = new List<string>(),
					BestValue = PokerHandValue.HighCard,
					BestRankValues = new List<int>(),
				},
				(agg, next) =>
				{
					switch (CompareHands(next.Hand, agg.BestValue, agg.BestRankValues))
					{
						case > 0:
							return new()
							{
								BestHands = new List<string>() { next.HandString },
								BestValue = next.Hand.HandValue,
								BestRankValues = next.Hand.Cards.Select(card => card.Value)
									.ToList(),
							};

						case 0:
							agg.BestHands.Add(next.HandString);
							break;
					}

					return agg;
				}
			)
			.BestHands;

	private static int CompareHands(
		PokerHand pokerHand,
		PokerHandValue bestValue,
		IReadOnlyList<int> bestRankValues
	)
	{
		if (pokerHand.HandValue > bestValue)
		{
			return 1;
		}

		if (pokerHand.HandValue < bestValue)
		{
			return -1;
		}

		if (bestValue is PokerHandValue.FullHouse or PokerHandValue.FourOfAKind)
		{
			string? pokerHandTripletValue
				= pokerHand.Cards.FindTripletKey(card => card.Rank);

			string? bestTripletValue
				= bestRankValues.FindTripletKey(rank => rank.ToString(CultureInfo.InvariantCulture));

			if (!string.IsNullOrEmpty(pokerHandTripletValue)
				&& !string.IsNullOrEmpty(bestTripletValue))
			{
				int tripletComparison = CompareTripletValues(pokerHandTripletValue, bestTripletValue);
				if (tripletComparison != 0)
				{
					return tripletComparison;
				}
			}
		}

		return CompareCardValues(pokerHand, bestRankValues);
	}

	private static int CompareTripletValues(string? pokerHandTripletValue, string? bestTripletValue)
		=> GetCardValue(pokerHandTripletValue!)
			.CompareTo(GetCardValue(bestTripletValue!));

	private static int CompareCardValues(PokerHand pokerHand, IReadOnlyList<int> bestRankValues)
	{
		List<int> cardValues = pokerHand.Cards
			.Select(card => card.Value)
			.ToList();
		return cardValues.CompareTo(bestRankValues);
	}

	private static int GetCardValue(string rank)
		=> PokerCard.ValueByRank.TryGetValue(rank, out int value)
			? value
			: throw new ArgumentException($"Invalid card rank: {rank}");
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class PokerHand
{
	public required IReadOnlyCollection<PokerCard> Cards { get; init; }

	private PokerHandValue? _handValue;

	public PokerHandValue HandValue
	{
		get
		{
			_handValue ??= EvaluateHandValue();
			return (PokerHandValue)_handValue;
		}
	}

	private PokerHandValue EvaluateHandValue()
	{
		IGrouping<string, PokerCard>[] rankGroups = Cards.GroupBy(card => card.Rank)
			.ToArray();

		return rankGroups.Any(group => group.Count() == 4)
			? PokerHandValue.FourOfAKind
			: rankGroups.Any(group => group.Count() == 3)
				? rankGroups.Any(group => group.Count() == 2)
					? PokerHandValue.FullHouse
					: PokerHandValue.ThreeOfAKind
				: rankGroups.Count(group => group.Count() == 2) == 2
					? PokerHandValue.TwoPair
					: rankGroups.Any(group => group.Count() == 2)
						? PokerHandValue.Pair
						: IsFlush()
							? IsLowAceStraight()
								? PokerHandValue.LowAceStraightFlush
								: IsStraight()
									? PokerHandValue.StraightFlush
									: PokerHandValue.Flush
							: IsLowAceStraight()
								? PokerHandValue.LowAceStraight
								: IsStraight()
									? PokerHandValue.Straight
									: PokerHandValue.HighCard;
	}

	private bool IsStraight()
	{
		List<int> sortedValues = Cards.Select(card => card.Value)
			.OrderBy(value => value)
			.ToList();

		return Enumerable.Range(sortedValues[0], 5)
			.SequenceEqual(sortedValues);
	}

	private bool IsLowAceStraight()
		=> Cards.Select(card => card.Rank)
			.OrderBy(rank => rank)
			.ToArray() is ["2", "3", "4", "5", "A"];

	private bool IsFlush()
	{
		string suit = Cards.First()
			.Suit;
		return Cards.All(card => card.Suit == suit);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public enum PokerHandValue
{
	HighCard,
	Pair,
	TwoPair,
	ThreeOfAKind,
	LowAceStraight,
	Straight,
	Flush,
	FullHouse,
	FourOfAKind,
	LowAceStraightFlush,
	StraightFlush,
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class BestHandInfo
{
	public required ICollection<string> BestHands { get; init; }
	public PokerHandValue BestValue { get; init; }
	public required IReadOnlyList<int> BestRankValues { get; init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class HandInfo
{
	public required string HandString { get; init; }

	private PokerHand? _hand;

	public PokerHand Hand
	{
		get
		{
			_hand ??= EvaluateHand();
			return _hand;
		}
	}

	private PokerHand EvaluateHand()
	{
		List<PokerCard> orderedCards = HandString
			.Split(' ')
			.Select(PokerCard.From)
			.OrderByDescending(card => card.Value)
			.ToList();

		return new() { Cards = orderedCards };
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class PokerCard
{
	public static readonly Dictionary<string, int> ValueByRank = new()
	{
		{ "2", 2 },
		{ "3", 3 },
		{ "4", 4 },
		{ "5", 5 },
		{ "6", 6 },
		{ "7", 7 },
		{ "8", 8 },
		{ "9", 9 },
		{ "10", 10 },
		{ "J", 11 },
		{ "Q", 12 },
		{ "K", 13 },
		{ "A", 14 },
	};

	public required string Rank { get; init; }
	public required string Suit { get; init; }

	public int Value
		=> GetCardValue();

	public static PokerCard From(string cardString)
		=> new() { Rank = cardString[..^1], Suit = cardString[^1..] };

	private int GetCardValue()
		=> ValueByRank.TryGetValue(Rank, out int value)
			? value
			: throw new ArgumentException($"Invalid card rank: {Rank}");
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class EnumerableExtensions
{
	public static TKey? FindTripletKey<T, TKey>(
		this IEnumerable<T> enumerable,
		Func<T, TKey> groupKeySelector
	)
		=> enumerable.FindGroupKey(groupKeySelector, group => group.Count() >= 3);

	private static TKey? FindGroupKey<T, TKey>(
		this IEnumerable<T> enumerable,
		Func<T, TKey> groupKeySelector,
		Func<IGrouping<TKey, T>, bool> condition
	)
	{
		IGrouping<TKey, T>? groups = enumerable
			.GroupBy(groupKeySelector)
			.FirstOrDefault(condition);

		return groups != null
			? groups.Key
			: default;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class ListExtensions
{
	internal static int CompareTo<T>(this IReadOnlyList<T> list1, IReadOnlyList<T> list2) where T : IComparable<T>
	{
		int minLength = Math.Min(list1.Count, list2.Count);
		for (int i = 0; i < minLength; i++)
		{
			int comparison = list1[i]
				.CompareTo(list2[i]);
			if (comparison != 0)
			{
				return comparison;
			}
		}

		return list1.Count.CompareTo(list2.Count);
	}
}

//=======================================================================