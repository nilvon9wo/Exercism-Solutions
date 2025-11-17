using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

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

	private static readonly Random _random = new();

	public static int Ability()
		=> Roll4Times()
			.OrderByDescending(r => r)
			.Take(3)
			.Sum();

	private static IEnumerable<int> Roll4Times()
		=> Enumerable.Range(0, 4)
			.Select(_ => Roll6SidedDie(_random));

	[SuppressMessage(
		"Security",
		"CA5394:Do not use insecure randomness",
		Justification = "We are not concerned with security"
	)]
	private static int Roll6SidedDie(Random random)
		=> random.Next(1, 7);

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