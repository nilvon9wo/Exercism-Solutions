using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class PigLatin
{
	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;
	private const char _separator = ' ';

	private static readonly HashSet<char> _vowels =
		new()
		{
			'a',
			'e',
			'i',
			'o',
			'u',
		};

	public static string Translate(string text)
	{
		if (string.IsNullOrWhiteSpace(text))
		{
			throw new ArgumentException($"'{nameof(text)}' cannot be null or whitespace.", nameof(text));
		}

		IEnumerable<string?> translatedWords = text.Split(_separator)
			.Select(TranslateWord);
		return string.Join(" ", translatedWords);
	}

	private static string TranslateWord(string text)
		=> text.StartsWithVowel()
			? text.AddAy()
			: text.TranslateWhenStartingWithConsonant();

	private static string TranslateWhenStartingWithConsonant(this string word)
	{
		int firstVowel = word.FindFirstVowel();
		return word.StartsWithQu()
			? $"{word[2..]}qu".AddAy()
			: word.HasQuPrecededByConsonant()
				? $"{word[3..]}{word[0]}qu".AddAy()
				: word.IsTwoLetterYWord()
					? $"{word[1]}{word[0]}".AddAy()
					: word.HasYConsonant(firstVowel)
						? word.TranslateWhenContainingYVowel()
						: $"{word[firstVowel..]}{word[..firstVowel]}".AddAy();
	}

	private static string TranslateWhenContainingYVowel(this string word)
	{
		int indexOfY = word.IndexOf("y", _invariantCulture);
		return $"{word[indexOfY..]}{word[..indexOfY]}".AddAy();
	}

	private static bool StartsWithVowel(this string word)
		=> _vowels.Contains(word[0])
		   || ((word.Length > 2)
			   && (
				   word.StartsWith("xr", _invariantCulture)
				   || word.StartsWith("yt", _invariantCulture)
			   )
		   );

	private static int FindFirstVowel(this string word)
		=> word.Select((c, index) => (c, index))
			.FirstOrDefault(pair => _vowels.Contains(pair.c))
			.index;

	private static bool IsTwoLetterYWord(this string word)
		=> (word.Length == 2) && word.Contains('y', _invariantCulture);

	private static bool HasYConsonant(this string word, int firstVowel)
	{
		int indexOfY = word.IndexOf("y", _invariantCulture);
		return IsConsonant(word[0])
			   && (indexOfY > 1)
			   && ((firstVowel == 0)
				   || (firstVowel > indexOfY));
	}

	private static bool IsConsonant(this char value)
		=> !_vowels.Contains(value);

	private static bool StartsWithQu(this string word)
		=> word.StartsWith("qu", _invariantCulture);

	private static bool HasQuPrecededByConsonant(this string word)
		=> word[0]
			   .IsConsonant()
		   && (word[1] == 'q')
		   && (word[2] == 'u');

	private static string AddAy(this string value)
		=> $"{value}ay";
}