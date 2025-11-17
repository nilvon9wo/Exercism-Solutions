using System;

public class Player
{
	private readonly Random _random;

	public Player(Random random = null) =>
		_random = random ?? new Random();

	private const int MinRollValue = 1;
	private const int MaxRollValue = 18;
	private const int MinSpellValue = 0;
	private const int MaxSpellValue = 100;

	public int RollDie() =>
		_random.Next(MinRollValue, MaxRollValue);

	public double GenerateSpellStrength() =>
		_random.Next(MinSpellValue, MaxSpellValue);
}
