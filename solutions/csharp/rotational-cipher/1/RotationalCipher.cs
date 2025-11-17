using System.Linq;

public static class RotationalCipher
{
	private const int _lettersInAlphbet = 26;
	private const char _lowercaseA = 'a';
	private const char _lowercaseZ = 'z';
	private const char _uppercaseA = 'A';
	private const char _uppercaseZ = 'Z';

	public static string Rotate(string text, int shiftKey)
	{
		int shiftValue = shiftKey % _lettersInAlphbet;
		char[] characters = text
			.Select(character =>
				character switch
				{
					>= _lowercaseA and <= _lowercaseZ =>
						RotateLower(character, shiftValue),

					>= _uppercaseA and <= _uppercaseZ =>
						RotateUpper(character, shiftValue),

					_ => character,
				})
			.ToArray();

		return new(characters);
	}

	private static char RotateLower(char character, int shiftValue) =>
		Rotate(character, shiftValue, _lowercaseZ);

	private static char RotateUpper(char character, int shiftValue) =>
		Rotate(character, shiftValue, _uppercaseZ);

	private static char Rotate(char character, int shiftValue, int upperLimit)
	{
		int result = character + shiftValue;
		return (result > upperLimit)
			? (char)(result - _lettersInAlphbet)
			: (char)result;
	}
}