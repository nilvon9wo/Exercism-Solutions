using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;

public class Anagram
{
	private readonly string _baseWord;
	private readonly Dictionary<char, int> _baseCountByCharacters;

	public Anagram(string baseWord)
	{
		_baseWord = baseWord.ToLowerInvariant();
		_baseCountByCharacters = CountCharacters(baseWord);
	}

	private static readonly CaseInsensitiveCharComparer _comparer = new();

	private static Dictionary<char, int> CountCharacters(string word) =>
		word.ToCharArray()
			.Aggregate(
			new Dictionary<char, int>(_comparer),
			(seed, character) =>
			{
				seed[character] = seed.TryGetValue(character, out int value)
					? ++value
					: 1;

				return seed;
			}
		);

	public string[] FindAnagrams(string[] potentialMatches) =>
		potentialMatches
			.Where(NotBaseWord)
			.Where(ContainsCorrectCharacterCounts)
			.ToArray();

	private bool NotBaseWord(string word) =>
		word.ToLowerInvariant() != _baseWord;

	private bool ContainsCorrectCharacterCounts(string word)
	{
		Dictionary<char, int> wordCountByCharacters = CountCharacters(word);
		return wordCountByCharacters.Keys.All(_baseCountByCharacters.ContainsKey)
			&& wordCountByCharacters.Keys.All(x => wordCountByCharacters[x] == _baseCountByCharacters[x]);
	}
}

public class CaseInsensitiveCharComparer : IEqualityComparer<char>
{
	private readonly CultureInfo _cultureInfo;

	public CaseInsensitiveCharComparer(CultureInfo cultureInfo) =>
		_cultureInfo = cultureInfo;

	public CaseInsensitiveCharComparer() : this(CultureInfo.CurrentCulture)
	{
	}

	public bool Equals(char x, char y) =>
		ToUpper(x) - ToUpper(y) == 0;

	private char ToUpper(char x) =>
		char.ToUpper(x, _cultureInfo);

	public int GetHashCode([DisallowNull] char obj)
	{
		unchecked
		{
			return (17 * 23) + _cultureInfo.GetHashCode();
		}
	}
}

