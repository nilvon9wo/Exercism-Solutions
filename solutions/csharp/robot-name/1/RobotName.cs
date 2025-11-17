using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

public class Robot
{
	private string _name;

	public string Name
	{
		get
		{
			_name ??= RobotNameFactory.GenerateName();
			return _name;
		}
	}

	public void Reset() =>
		_name = null;
}

public class RobotNameFactory
{
	private static readonly List<string> allNames = new();

	public static string GenerateName()
	{
		string name;
		do
		{
			name = new StringBuilder()
				.Append(RandomCharacterProvider.RandomLetter(2))
				.Append(RandomCharacterProvider.RandomDigit(3))
				.ToString();
		}
		while (allNames.Contains(name));

		allNames.Add(name);
		return name;
	}
}

public class RandomCharacterProvider
{
	private static readonly Random _random = new();

	private const string letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private const string digits = "0123456789";

	public static char[] RandomLetter(int quantity) =>
		RandomCharacter(letters, quantity);

	public static char[] RandomDigit(int quantity) =>
		RandomCharacter(digits, quantity);

	private static char[] RandomCharacter(string choices, int quantity) =>
		Enumerable.Range(0, quantity)
			.Select(i =>
				choices[_random.Next(0, choices.Length)]
			)
			.ToArray();
}