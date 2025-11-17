using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;

// ReSharper disable once CheckNamespace

public class DndCharacter
{
	public int Strength { get; private init; }
	public int Dexterity { get; private init; }
	public int Constitution { get; private init; }
	public int Intelligence { get; private init; }
	public int Wisdom { get; private init; }
	public int Charisma { get; private init; }
	public int Hitpoints { get; private init; }

	public static int Modifier(int score)
		=> (int)Math.Floor((score - 10) / 2.0);

	public static int Ability()
		=> Roll4Times()
			.OrderByDescending(r => r)
			.Take(3)
			.Sum();

	private static IEnumerable<int> Roll4Times()
		=> Enumerable.Range(0, 4)
			.Select(_ => SixSidedDie.Roll());

	public static DndCharacter Generate()
	{
		int constitution = Ability();
		return new()
		{
			Strength = Ability(),
			Dexterity = Ability(),
			Constitution = constitution,
			Intelligence = Ability(),
			Wisdom = Ability(),
			Charisma = Ability(),
			Hitpoints = 10 + Modifier(constitution),
		};
	}
}

// ReSharper disable once CheckNamespace
internal static class SixSidedDie
{
	private const int _minValue = 1; // Minimum value for a 6-sided die
	private const int _maxValue = 7; // Maximum value for a 6-sided die

	private static readonly RandomNumberGenerator _random = RandomNumberGenerator.Create();

	internal static int Roll()
	{
		byte[] randomNumber = GenerateRandomBytes();
		int randomValue = BitConverter.ToInt32(randomNumber, 0);
		return MapToSixSidedDie(randomValue);
	}

	private static byte[] GenerateRandomBytes()
	{
		byte[] randomNumber = new byte[4];
		_random.GetBytes(randomNumber);
		return randomNumber;
	}

	private static int MapToSixSidedDie(int randomValue)
		=> _minValue + (Math.Abs(randomValue) % (_maxValue - _minValue));
}