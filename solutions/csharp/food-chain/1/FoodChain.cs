#nullable enable

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

public static class FoodChain
{
	private static readonly Dictionary<int, Animal> _animalByNumber = new()
	{
		{ 1, new Animal("fly") },
		{ 2, new Animal("spider", "It wriggled and jiggled and tickled inside her.", true) },
		{ 3, new Animal("bird", "How absurd to swallow a bird!") },
		{ 4, new Animal("cat", "Imagine that, to swallow a cat!") },
		{ 5, new Animal("dog", "What a hog, to swallow a dog!") },
		{ 6, new Animal("goat", "Just opened her throat and swallowed a goat!") },
		{ 7, new Animal("cow", "I don't know how she swallowed a cow!") },
		{ 8, new Animal("horse", "She's dead, of course!") },
	};
	private static readonly int _lastAnimal = _animalByNumber.Keys.Max();

	public static string Recite(int verseNumber)
	{
		if (!_animalByNumber.TryGetValue(verseNumber, out Animal animal))
		{
			throw new ArgumentOutOfRangeException(nameof(verseNumber), $"Neither animal nor verse exists for {verseNumber}");
		}

		StringBuilder stringBuilder = new($"I know an old lady who swallowed {animal.IndefiniteForm}.");
		if (!string.IsNullOrEmpty(animal.Remark))
		{
			stringBuilder = stringBuilder.Append($"\n{animal.Remark}");
		}

		StringBuilder refrain = CreateRefrain(verseNumber);
		return stringBuilder.Append(refrain)
			.ToString();

	}

	private static StringBuilder CreateRefrain(int verseNumber)
	{
		StringBuilder stringBuilder = new();
		if (verseNumber != _lastAnimal)
		{
			stringBuilder = stringBuilder.Append('\n');

			for (int animalNumber = verseNumber; animalNumber > 1; animalNumber--)
			{
				string swallowLine = CreateSwallowLine(animalNumber);
				stringBuilder = stringBuilder.Append(swallowLine);
			}

			stringBuilder = stringBuilder.Append("I don't know why she swallowed the fly. Perhaps she'll die.");
		}

		return stringBuilder;
	}

	private static string CreateSwallowLine(int animalNumber)
	{
		Animal currentAnimal = _animalByNumber[animalNumber];
		Animal previousAnimal = _animalByNumber[animalNumber - 1];
		string lineEnd = previousAnimal.HasExtendVerse
			? $" {previousAnimal.VersExtension}"
			: ".";
		return $"She swallowed the {currentAnimal.Name} to catch the {previousAnimal.Name}{lineEnd}\n";
	}

	public static string Recite(int startVerse, int endVerse)
	{
		List<string> verses = Enumerable.Range(startVerse, endVerse)
			.Select(x => Recite(x))
			.ToList();
		return string.Join("\n\n", verses.ToArray());
	}
}

public record struct Animal(string Name, string? Remark = null, bool HasExtendVerse = false)
{
	public string IndefiniteForm =>
		Name.PrefixArticle();

	public string VersExtension =>
		(!HasExtendVerse || string.IsNullOrWhiteSpace(Remark))
			? throw new InvalidOperationException($"{Name} does not have extended verse")
			: Remark.Replace("It", "that");
}

public static class StringExtensions
{
	private static readonly HashSet<char> vowels = new()
	{
		'a', 'e', 'i', 'o', 'u'
	};

	public static string PrefixArticle(this string text) =>
		string.IsNullOrWhiteSpace(text)
			? throw new ArgumentException($"'{nameof(text)}' cannot be null or whitespace.", nameof(text))
			: StartsWithVowel(text)
				? $"an {text}"
				: $"a {text}";

	private static bool StartsWithVowel(string text)
	{
		char firstCharacter = char.ToLowerInvariant(text[0]);
		return vowels.Contains(firstCharacter);
	}
}