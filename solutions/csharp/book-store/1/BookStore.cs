using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class BookStore
{
	private const int _bookCost = 8;
	private const int _negotiableDiscountQuantity = 3;
	private const int _optimalDiscountQuantity = 4;

	private static readonly Dictionary<int, decimal> _discountsByDifferentBooks = new()
	{
		{ 0, 0.00m },
		{ 1, 0.00m },
		{ 2, 0.05m },
		{ 3, 0.10m },
		{ 4, 0.20m },
		{ 5, 0.25m },
	};

	private static readonly int _completeSetQuantity = _discountsByDifferentBooks.Keys.Max();

	public static decimal Total(IEnumerable<int> bookIdsEnumerable)
	{
		int[] bookIds = bookIdsEnumerable.ToArray();
		int numberOfBooks = bookIds.Length;
		Dictionary<int, int> quantityByBookIds = CreateQuantityDictionary(bookIds);
		return IsNoDiscount(numberOfBooks, quantityByBookIds)
			? NormalCost(numberOfBooks)
			: IsSimpleDiscount(quantityByBookIds)
				? DiscountedCost(numberOfBooks)
				: MakeComplexDiscount(quantityByBookIds);
	}

	private static decimal MakeComplexDiscount(Dictionary<int, int> quantityByBookIds)
	{
		IEnumerable<HashSet<int>> groupedBooks
			= GroupTowardsSeriesCompletion(quantityByBookIds);
		return RegroupTowardsMaximumDiscount(groupedBooks)
			.Select(CreateQuantityDictionary)
			.Sum(groupedBookCount => DiscountedCost(groupedBookCount.Values.Count));
	}

	private static Dictionary<int, int> CreateQuantityDictionary(IEnumerable<int> bookIds)
		=> bookIds.GroupBy(value => value)
			.ToDictionary(group => group.Key, group => group.Count());

	private static bool IsNoDiscount(int numberOfBooks, Dictionary<int, int> quantityByBooks)
		=> (numberOfBooks < 2) || AreAllBooksTheSame(numberOfBooks, quantityByBooks);

	private static bool AreAllBooksTheSame(int numberOfBooks, Dictionary<int, int> quantityByBooks)
		=> quantityByBooks.Values.Any(quantity => quantity == numberOfBooks);

	private static bool IsSimpleDiscount(Dictionary<int, int> quantityByBooks)
		=> quantityByBooks.Values.All(quantity => quantity == 1);

	private static decimal DiscountedCost(int numberOfBooks)
		=> NormalCost(numberOfBooks) - DiscountForDifferentBooks(numberOfBooks);

	private static decimal NormalCost(int numberOfBooks)
		=> numberOfBooks * _bookCost;

	private static decimal DiscountForDifferentBooks(int numberOfBooks)
		=> _bookCost * numberOfBooks * _discountsByDifferentBooks[numberOfBooks];

	private static IEnumerable<HashSet<int>> RegroupTowardsMaximumDiscount(
		IEnumerable<HashSet<int>> groupedTowardsSeriesCompletionEnumerable
	)
	{
		HashSet<int>[] groupedTowardsSeriesCompletion = groupedTowardsSeriesCompletionEnumerable.ToArray();
		Dictionary<int, List<HashSet<int>>> groupedBySetSize
			= groupedTowardsSeriesCompletion.GroupBy(set => set.Count)
				.ToDictionary(group => group.Key, group => group.ToList());

		return IsRegroupingUseless(groupedBySetSize)
			? groupedTowardsSeriesCompletion
			: RegroupTowardsMaximumDiscount(groupedBySetSize);
	}

	private static IEnumerable<HashSet<int>> RegroupTowardsMaximumDiscount(
		IReadOnlyDictionary<int, List<HashSet<int>>> groupedBySetSize
	)
	{
		IReadOnlyCollection<HashSet<int>> completeGroup
			= groupedBySetSize.GetValueOrDefault(_completeSetQuantity, new());
		IReadOnlyCollection<HashSet<int>> groupsOf3
			= groupedBySetSize.GetValueOrDefault(_negotiableDiscountQuantity, new());
		int smallerSetSize = Math.Min(completeGroup.Count, groupsOf3.Count);
		return new Dictionary<int, List<HashSet<int>>>(groupedBySetSize)
		{
			[_completeSetQuantity] = completeGroup.Skip(smallerSetSize)
					.ToList(),
			[_negotiableDiscountQuantity] = groupsOf3.Skip(smallerSetSize)
					.ToList(),
			[_optimalDiscountQuantity] = RegroupBooksForMaximumDiscount(groupedBySetSize, completeGroup, groupsOf3),
		}
			.Values
			.SelectMany(list => list);
	}

	private static List<HashSet<int>> RegroupBooksForMaximumDiscount(
		IReadOnlyDictionary<int, List<HashSet<int>>> groupedBySetSize,
		IReadOnlyCollection<HashSet<int>> completeGroup,
		IReadOnlyCollection<HashSet<int>> groupsOf3
	)
	{
		int smallerSetSize = Math.Min(completeGroup.Count, groupsOf3.Count);
		IReadOnlyCollection<HashSet<int>> completeGroupsToRedistribute = completeGroup.Take(smallerSetSize)
			.ToList();
		IReadOnlyCollection<HashSet<int>> groupsOf3ToAugment = groupsOf3.Take(smallerSetSize)
			.ToList();
		return RegroupTowardsMaximumDiscount(completeGroupsToRedistribute, groupsOf3ToAugment)
			.Concat(groupedBySetSize.GetValueOrDefault(4, new()))
			.ToList();
	}

	private static IEnumerable<HashSet<int>> RegroupTowardsMaximumDiscount(
		IEnumerable<HashSet<int>> completeGroupsToRedistribute,
		IEnumerable<HashSet<int>> groupsOf3ToAugment
	)
	{
		IEnumerable<(HashSet<int>, HashSet<int>)> regroupedBooks = groupsOf3ToAugment.Zip(completeGroupsToRedistribute)
			.Select(RegroupTowardsMaximumDiscount);
		(IEnumerable<HashSet<int>> groupOf41, IEnumerable<HashSet<int>> groupOf42) = regroupedBooks.Unzip();
		return groupOf41.Concat(groupOf42);
	}

	private static (HashSet<int>, HashSet<int>) RegroupTowardsMaximumDiscount(
		(HashSet<int> partialSet, HashSet<int> completeSet) pairToRegroup
	)
	{
		(HashSet<int> receiverSet, HashSet<int> giverSet) = pairToRegroup;

		int missingBook = Enumerable
			.Range(1, _completeSetQuantity)
			.Select(bookNumber => (bookNumber, receiverSet.Contains(bookNumber)))
			.First(pair => !IsContained(pair))
			.bookNumber;

		_ = receiverSet.Add(missingBook);
		_ = giverSet.Remove(missingBook);

		return (receiverSet.ToHashSet(), giverSet.ToHashSet());
	}

	private static bool IsContained((int bookNumber, bool contained) bookContained)
		=> bookContained.contained;

	// For maximum discount, when possible,
	// need to move books from sets of 5 into sets of 3 (resulting in 2 sets of 4 each)
	private static bool IsRegroupingUseless(IReadOnlyDictionary<int, List<HashSet<int>>> groupedBySetSize)
		=> !(groupedBySetSize.TryGetValue(_completeSetQuantity, out List<HashSet<int>>? completeSets)
			 && (completeSets.Count > 0))
		   && !(groupedBySetSize.TryGetValue(
					_negotiableDiscountQuantity,
					out List<HashSet<int>>? negotiableDiscountSets
				)
				&& (negotiableDiscountSets.Count > 0));

	private static IEnumerable<HashSet<int>> GroupTowardsSeriesCompletion(Dictionary<int, int> quantityByBookIds)
		=> quantityByBookIds.Aggregate(
				new Dictionary<int, HashSet<int>>(),
				GroupTowardsSeriesCompletion
			)
			.Values;

	private static Dictionary<int, HashSet<int>> GroupTowardsSeriesCompletion(
		Dictionary<int, HashSet<int>> groupedBooks,
		KeyValuePair<int, int> keyValuePair
	)
	{
		int bookId = keyValuePair.Key;
		int quantity = keyValuePair.Value;

		return Enumerable.Range(0, quantity)
			.Aggregate(
				groupedBooks,
				(currentGroupedBooks, i) =>
				{
					if (!currentGroupedBooks.TryGetValue(i, out HashSet<int>? value))
					{
						value = new();
						currentGroupedBooks[i] = value;
					}

					_ = value.Add(bookId);
					return currentGroupedBooks;
				}
			);
	}
}

// ReSharper disable once CheckNamespace
public static class EnumerableExtensions
{
	public static (IEnumerable<T1>, IEnumerable<T2>) Unzip<T1, T2>(this IEnumerable<ValueTuple<T1, T2>> zippedSequence)
	{
		_ = zippedSequence ?? throw new ArgumentNullException(nameof(zippedSequence));
		List<T1> firstList = new();
		List<T2> secondList = new();

		foreach ((T1 first, T2 second) in zippedSequence)
		{
			firstList.Add(first);
			secondList.Add(second);
		}

		return (firstList, secondList);
	}
}