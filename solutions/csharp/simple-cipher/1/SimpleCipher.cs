using System;
using System.Text;

public class SimpleCipher
{
	public SimpleCipher()
	{
		Key = RandomStringGenerator.CreateString(100);
	}

	public SimpleCipher(string key)
	{
		Key = key;
	}

	public string Key { get; }

	public string Encode(string plaintext)
	{
		return Convert(plaintext, (character, index) =>
		{
			int plainCharacterValue = character.ToZeroIndexedValue();
			int keyCharacterValue = GetKeyCharacterValue(index);
			return (plainCharacterValue + keyCharacterValue)
				.ToZeroIndexedLetter();
		});
	}

	public string Decode(string ciphertext)
	{
		return Convert(ciphertext, (character, index) =>
		{
			int plainCharacterValue = ciphertext[index].ToZeroIndexedValue();
			int keyCharacterValue = GetKeyCharacterValue(index);
			return (plainCharacterValue - keyCharacterValue)
				.ToZeroIndexedLetter();
		});
	}

	private string Convert(string text, Func<char, int, char> convert)
	{
		StringBuilder builder = new();
		for (int i = 0; i < text.Length; i++)
		{
			char resultCharacter = convert(text[i], i);
			builder = builder.Append(resultCharacter);
		}

		return builder.ToString();
	}

	private int GetKeyCharacterValue(int i)
	{
		int keyIndex = i % Key.Length;
		return Key[keyIndex].ToZeroIndexedValue();
	}
}

public class RandomStringGenerator
{
	private const string AllowedChars = "abcdefghijklmnopqrstuvwxyz";
	private static readonly Random _random = new();

	public static string CreateString(int length)
	{
		StringBuilder builder = new();
		for (int i = 0; i < length; i++)
		{
			char chosenCharacter = AllowedChars[_random.Next(0, AllowedChars.Length)];
			builder = builder.Append(chosenCharacter);
		}

		return builder.ToString();
	}
}

public static class CharExtensions
{
	private const int ValueOfLowerCaseA = 'a';
	private const int ValueOfLowerCaseZ = 'z';
	private const int AlphabetSize = ValueOfLowerCaseZ - ValueOfLowerCaseA;

	public static int ToZeroIndexedValue(this char character)
	{
		return character - ValueOfLowerCaseA;
	}

	public static char ToZeroIndexedLetter(this int value)
	{
		int characterValue = value switch
		{
			< 0 =>
				ValueOfLowerCaseZ + value + 1,

			> AlphabetSize =>
				ValueOfLowerCaseA - AlphabetSize + value - 1,

			_ =>
				value + ValueOfLowerCaseA
		};

		return Convert.ToChar(characterValue);
	}
}