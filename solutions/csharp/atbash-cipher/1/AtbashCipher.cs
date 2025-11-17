using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;

// ReSharper disable once CheckNamespace
public static class AtbashCipher
{
	private const string _latinAlphabet = "abcdefghijklmnopqrstuvwxyz";

	private static readonly Dictionary<char, char> _reverseAlphabetByLatinCharacters = _latinAlphabet
		.ToDictionary(c => c, c => (char)('z' - (c - 'a')));

	private static readonly Dictionary<char, char> _originalAlphabetByReverseCharacters =
		_reverseAlphabetByLatinCharacters
			.ToDictionary(pair => pair.Value, pair => pair.Key);

	public static string Encode(string plainText)
	{
		_ = plainText ?? throw new ArgumentNullException(nameof(plainText));
		string encryptedText = Transcribe(plainText, _reverseAlphabetByLatinCharacters);
		return Grouper.Group(encryptedText);
	}

	public static string Decode(string encodedValue)
	{
		_ = encodedValue ?? throw new ArgumentNullException(nameof(encodedValue));
		return Transcribe(encodedValue, _originalAlphabetByReverseCharacters);
	}

	private static string Transcribe(string plainText, Dictionary<char, char> characterMap)
		=> plainText.Aggregate(
				new StringBuilder(),
				(stringBuilder, character) => Transcriber.Transcribe(stringBuilder, character, characterMap)
			)
			.ToString();
}

public static class Transcriber
{
	private static readonly
		Dictionary<CharacterType, Func<StringBuilder, char, Dictionary<char, char>, StringBuilder>>
		_characterHandlerByCharacterType =
			new()
			{
				{ CharacterType.Letter, HandleLetter },
				{ CharacterType.Number, HandleNumber },
				{ CharacterType.Other, HandleOther }
			};

	private static readonly CultureInfo _invariantCultureInfo = CultureInfo.InvariantCulture;

	public static StringBuilder Transcribe(
		StringBuilder encryptedText, char character, Dictionary<char, char> characterMap
	)
	{
		CharacterType characterType = GetCharacterType(character);
		Func<StringBuilder, char, Dictionary<char, char>, StringBuilder> handler =
			_characterHandlerByCharacterType[characterType];
		return handler(encryptedText, character, characterMap);
	}

	private static StringBuilder HandleLetter(
		StringBuilder encryptedText, char character, Dictionary<char, char> characterMap
	)
	{
		char lowerCaseCharacter = char.ToLower(character, _invariantCultureInfo);
		char encryptedChar = characterMap[lowerCaseCharacter];
		return encryptedText.Append(encryptedChar);
	}

	private static StringBuilder HandleNumber(
		StringBuilder encryptedText, char character, Dictionary<char, char> characterMap
	)
		=> encryptedText.Append(character);

	private static StringBuilder HandleOther(
		StringBuilder encryptedText, char character, Dictionary<char, char> characterMap
	)
		=> encryptedText;

	private static CharacterType GetCharacterType(char character)
		=> char.IsLetter(character)
			? CharacterType.Letter
			: char.IsDigit(character)
				? CharacterType.Number
				: CharacterType.Other;
}

public static class Grouper
{
	private const int _groupSize = 5;

	public static string Group(string text)
	{
		_ = text ?? throw new ArgumentNullException(nameof(text));
		StringBuilder groupedText = new();
		int count = 0;

		foreach (char letter in text)
		{
			if (count == _groupSize)
			{
				_ = groupedText.Append(' ');
				count = 0;
			}

			_ = groupedText.Append(letter);
			count++;
		}

		return groupedText.ToString();
	}
}

public enum CharacterType
{
	Letter,
	Number,
	Other
}