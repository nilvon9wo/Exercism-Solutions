using System;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;
using System.Text;

// ReSharper disable once CheckNamespace
public static class AffineCipher
{
	private static readonly CultureInfo _invariantCulture = CultureInfo.InvariantCulture;
	private const int _lengthOfRomanAlphabet = 26;
	private const char _alphabetStart = 'a';
	private const int _groupSize = 5;

	public static string Encode(string plainText, int multiplicativeKey, int additiveKey)
	{
		if (plainText is null)
		{
			throw new ArgumentNullException(nameof(plainText));
		}

		if (!IsCoprime(multiplicativeKey))
		{
			throw new ArgumentException("a and m must be coprime for encryption.");
		}

		string encryptedText = plainText
			.Where(char.IsLetterOrDigit)
			.Aggregate(
				new(),
				ProcessCharacterForEncoding(multiplicativeKey, additiveKey)
			)
			.ToString();

		return GroupLetters(encryptedText);
	}

	private static Func<StringBuilder, char, StringBuilder> ProcessCharacterForEncoding(
		int multiplicativeKey,
		int additiveKey
	)
		=> (stringBuilder, character) =>
		{
			if (char.IsLetter(character))
			{
				char encryptedChar = EncryptChar(character, multiplicativeKey, additiveKey);
				_ = stringBuilder.Append(encryptedChar);
			}
			else if (char.IsDigit(character))
			{
				_ = stringBuilder.Append(character);
			}

			return stringBuilder;
		};

	private static char EncryptChar(char character, int multiplicativeKey, int additiveKey)
	{
		int index = char.ToLower(character, _invariantCulture) - _alphabetStart;
		int encryptedIndex = ((multiplicativeKey * index) + additiveKey) % _lengthOfRomanAlphabet;
		return (char)(encryptedIndex + _alphabetStart);
	}

	public static string Decode(string cipheredText, int multiplicativeKey, int additiveKey)
		=> string.IsNullOrWhiteSpace(cipheredText)
			? throw new ArgumentException(
				$"'{nameof(cipheredText)}' cannot be null or whitespace.",
				nameof(cipheredText)
			)
			: !IsCoprime(multiplicativeKey)
				? throw new ArgumentException("a and m must be coprime for decryption.")
				: cipheredText
					.Aggregate(
						new(),
						ProcessCharacterForDecoding(multiplicativeKey, additiveKey)
					)
					.ToString();

	private static Func<StringBuilder, char, StringBuilder> ProcessCharacterForDecoding(
		int multiplicativeKey,
		int additiveKey
	)
		=> (stringBuilder, character) =>
		{
			if (char.IsLetter(character))
			{
				char decryptedChar = DecryptChar(character, multiplicativeKey, additiveKey);
				_ = stringBuilder.Append(decryptedChar);
			}
			else if (char.IsDigit(character))
			{
				_ = stringBuilder.Append(character);
			}

			return stringBuilder;
		};

	[SuppressMessage("Style", "IDE0047:Remove unnecessary parentheses", Justification = "Conflicting rules.")]
	private static char DecryptChar(char character, int multiplicativeKey, int additiveKey)
	{
		int index = char.ToLower(character, _invariantCulture) - _alphabetStart;
		int aInverse = FindModularMultiplicativeInverse(multiplicativeKey);

		int decryptedIndex = (aInverse * ((index - additiveKey) + _lengthOfRomanAlphabet)) % _lengthOfRomanAlphabet;
		if (decryptedIndex < 0)
		{
			decryptedIndex += _lengthOfRomanAlphabet;
		}

		return (char)(decryptedIndex + _alphabetStart);
	}

	private static bool IsCoprime(int additiveKey)
		=> Enumerable.Range(2, _lengthOfRomanAlphabet - 2)
			.All(i => ((_lengthOfRomanAlphabet % i) != 0) || ((additiveKey % i) != 0));

	private static int FindModularMultiplicativeInverse(int additiveKey)
	{
		int modulus = _lengthOfRomanAlphabet;
		int originalModulus = modulus;
		int previousX = 0;
		int currentX = 1;

		while (additiveKey > 1)
		{
			int quotient = additiveKey / modulus;
			int remainder = additiveKey % modulus;

			additiveKey = modulus;
			modulus = remainder;

			int temp = previousX;
			previousX = currentX - (quotient * previousX);
			currentX = temp;
		}

		if (currentX < 0)
		{
			currentX += originalModulus;
		}

		return currentX;
	}

	private static string GroupLetters(string text)
		=> text
			.Select((character, index) => (Character: character, Index: index))
			.Aggregate(new StringBuilder(), AppendCharactersAndSpaces)
			.ToString()
			.Trim();

	private static StringBuilder AppendCharactersAndSpaces(
		StringBuilder stringBuilder,
		(char Character, int Index) item
	)
	{
		stringBuilder = stringBuilder.Append(item.Character);
		return ShouldAppendSpace(item)
			? stringBuilder.Append(' ')
			: stringBuilder;
	}

	private static bool ShouldAppendSpace((char Character, int Index) item)
		=> (((item.Index + 1) % _groupSize) == 0) && (item.Index > 0);
}