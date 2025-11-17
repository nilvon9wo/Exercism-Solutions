using System;
using System.Collections.Generic;
using System.Linq;

public static class Dominoes
{
	public static bool CanChain(IEnumerable<(int, int)> dominoTuples)
	{
		List<Domino> dominoes = dominoTuples
			.Select(x => new Domino(x))
			.ToList();
		return !CountValues(dominoes)
					.Values.Any(x => x % 2 != 0)
						&& CanChainAll(dominoes);
	}

	private static bool CanChainAll(List<Domino> unchainedDominos)
	{
		DominoChain chainedDominos = DominoChain.From(ref unchainedDominos);
		return (!unchainedDominos.Any() || CanJoin(ref chainedDominos, ref unchainedDominos))
			&& chainedDominos.HasMatchingEnds();
	}

	private static bool CanJoin(ref DominoChain chainedDominos, ref List<Domino> unchainedDominos)
	{
		bool canJoinAll = true;
		while (canJoinAll && chainedDominos.TryGetIntersection(unchainedDominos, out HashSet<int> intersectingValues))
		{
			Domino matchingDomino = unchainedDominos.ExtractMatch(x => x.Values.Intersect(intersectingValues).Any());
			DominoChain secondChain = DominoChain.From(matchingDomino, ref unchainedDominos);
			canJoinAll = chainedDominos.TryJoin(secondChain);
		}

		return canJoinAll
			&& !unchainedDominos.Any();
	}

	private static Dictionary<int, int> CountValues(List<Domino> dominoes) =>
		dominoes.SelectMany(x => x.Values)
			.ToList()
			.GroupBy(value => value)
			.Select(value => new
			{
				Value = value.Key,
				Count = value.Count()
			})
			.ToDictionary(
			   group => group.Value,
			   group => group.Count
			);
}

public class Domino
{
	public int Left { get; init; }
	public int Right { get; init; }

	public Domino((int, int) values) =>
		(Left, Right) = values;

	public int[] Values =>
		new[] { Left, Right };

	internal bool HasMatch(HashSet<int> values) =>
		values.Contains(Left)
		|| values.Contains(Right);

	public Domino Reverse() =>
		new((Right, Left));

	public override string ToString() =>
		$"[{Left}, {Right}]";
}

public class DominoChain
{
	private LinkedList<Domino> _items = new();

	public static DominoChain From(ref List<Domino> unchainedDominos) =>
		new DominoChain()
			.TryAdd(ref unchainedDominos);

	public static DominoChain From(Domino matchingDomino, ref List<Domino> unchainedDominos)
	{
		DominoChain chain = new();
		_ = chain.TryAdd(matchingDomino);
		return chain.TryAdd(ref unchainedDominos);
	}

	private const int _noValue = -1;
	private const bool _failure = false;
	private const bool _success = true;

	private int? _leftMost =>
		_items.FirstOrDefault()
					?.Left;

	private int? _rightMost =>
		_items.LastOrDefault()
					?.Right;

	public DominoChain TryAdd(ref List<Domino> unchainedDominos)
	{
		if (!unchainedDominos.Any())
		{
			return this;
		}

		if (_items.Count == 0)
		{
			Domino first = unchainedDominos.Shift();
			_ = _items.AddFirst(first);
		}

		bool wasFailure = false;
		while (unchainedDominos.Any() && !wasFailure)
		{
			Domino domino = unchainedDominos.ExtractMatch(x =>
				x.HasMatch(new() { _leftMost ?? _noValue, _rightMost ?? _noValue })
			);
			wasFailure = !TryAdd(domino);
		}

		return this;
	}

	private bool TryAdd(Domino unchainedDomino)
	{
		switch (unchainedDomino)
		{
			case null:
				return _failure;

			default:
				if (!_items.Any())
				{
					_ = _items.AddFirst(unchainedDomino);
					return _success;
				}
				else if (unchainedDomino.Left == _leftMost)
				{
					_ = _items.AddFirst(unchainedDomino.Reverse());
					return _success;
				}
				else if (unchainedDomino.Right == _leftMost)
				{
					_ = _items.AddFirst(unchainedDomino);
					return _success;
				}
				else if (unchainedDomino.Left == _rightMost)
				{
					_ = _items.AddLast(unchainedDomino);
					return _success;
				}
				else if (unchainedDomino.Right == _rightMost)
				{
					_ = _items.AddLast(unchainedDomino.Reverse());
					return _success;
				}
				else
				{
					throw new ArgumentException($"{unchainedDomino} cannot be added.", nameof(unchainedDomino));
				}
		}
	}

	public HashSet<int> Values =>
		_items.SelectMany(x => x.Values)
			.ToHashSet();

	public bool TryGetIntersection(DominoChain secondChain, out HashSet<int> intersection) =>
		TryGetIntersection(secondChain._items, out intersection);

	public bool TryGetIntersection(IEnumerable<Domino> unchainedDominos, out HashSet<int> intersection)
	{
		intersection = unchainedDominos.SelectMany(x => x.Values)
			.Intersect(Values)
			.ToHashSet();
		return intersection.Any();
	}

	public bool HasMatchingEnds() =>
		_leftMost == _rightMost;

	public bool TryJoin(DominoChain secondChain)
	{
		if (TryGetIntersection(secondChain, out HashSet<int> intersection))
		{
			int intersectionValue = intersection.First();
			secondChain = secondChain.RotateUntilLeftValueIs(intersectionValue);
			_items = _items.InsertAfter(secondChain._items, x => x.Right == intersectionValue);
		}
		else
		{
			return false;
		}

		return true;
	}

	private DominoChain RotateUntilLeftValueIs(int intersectionValue)
	{
		if (!Values.Contains(intersectionValue))
		{
			throw new ArgumentException($"Chain does not contain {intersectionValue}.", nameof(intersectionValue));
		}

		while (_leftMost != intersectionValue)
		{
			Domino domino = _items.Shift();
			_ = _items.AddLast(domino);
		}

		return this;
	}
}

public static class CollectionExtensions
{
	public static T Shift<T>(this ICollection<T> haystack)
	{
		T value = haystack.FirstOrDefault();
		_ = haystack.Remove(value);
		return value;
	}

	public static LinkedList<T> InsertAfter<T>(
			this LinkedList<T> firstList,
			LinkedList<T> secondList,
			Func<T, bool> predicate
		) =>
		new(
				TakeUntilInclusive(firstList, predicate)
				.Concat(secondList)
				.Concat(firstList)
			);

	public static IEnumerable<T> TakeUntilInclusive<T>(this ICollection<T> collection, Func<T, bool> predicate)
	{
		List<T> newList = collection.TakeWhile(x => !predicate(x))
			.ToList();
		T matchingValue = collection.Shift();
		newList.Add(matchingValue);
		return newList;
	}

	public static int IndexOf<T>(this LinkedList<T> list, Func<T, bool> predicate)
	{
		int count = 0;
		for (LinkedListNode<T> node = list.First; node != null; node = node.Next, count++)
		{
			if (predicate(node.Value))
			{
				return count;
			}
		}

		return -1;
	}

	public static T ExtractMatch<T>(this List<T> haystack, Func<T, bool> func)
	{
		for (int i = 0; i < haystack.Count; i++)
		{
			T value = haystack[i];
			bool isMatch = func(value);
			if (isMatch)
			{
				haystack.RemoveAt(i);
				return value;
			}
		}

		return default;
	}
}