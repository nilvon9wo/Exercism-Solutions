using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;

public static class ParallelLetterFrequency
{
	public static Dictionary<char, int> Calculate(IEnumerable<string> texts)
	{
		return texts
			.AsParallel()
			.Aggregate(new CountingDictionary(), Calculate)
			.ToDictionary();
	}

	private static CountingDictionary Calculate(CountingDictionary seed, string text)
	{
		return text
			.Aggregate(
				seed,
				(innerSeed, character) =>
				{
					if (char.IsLetter(character))
					{
						innerSeed[character]++;
					}

					return innerSeed;
				}
			);
	}
}

public class CountingDictionary
{
	private static readonly CaseInsensitiveCharComparer comparer = new();
	private readonly ConcurrentDictionary<char, int> _countByLetters = new(comparer);

	public int this[char key]
	{
		get => _countByLetters.TryGetValue(key, out int count)
				? count
				: 0;

		set
		{
			if (!_countByLetters.ContainsKey(key))
			{
				_countByLetters[key] = 0;
			}

			_countByLetters[key] = value;
		}
	}

	internal Dictionary<char, int> ToDictionary()
	{
		return _countByLetters.ToDictionary(
			kvp => kvp.Key,
			kvp => kvp.Value,
			_countByLetters.Comparer
		);
	}
}

public class CaseInsensitiveCharComparer : IEqualityComparer<char>
{
	private readonly CultureInfo _cultureInfo;
	public CaseInsensitiveCharComparer(CultureInfo ci)
	{
		_cultureInfo = ci;
	}
	public CaseInsensitiveCharComparer() : this(CultureInfo.CurrentCulture) { }

	public bool Equals(char x, char y)
	{
		return ToUpper(x) - ToUpper(y) == 0;
	}

	private char ToUpper(char x)
	{
		return char.ToUpper(x, _cultureInfo);
	}

	public int GetHashCode([DisallowNull] char obj)
	{
		unchecked
		{
			return (17 * 23) + _cultureInfo.GetHashCode();
		}
	}
}
