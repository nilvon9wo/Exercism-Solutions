using System;

internal static class GameMaster
{
	public static string Describe(Character character)
	{
		return $"You're a level {character.Level} {character.Class} with {character.HitPoints} hit points.";
	}

	public static string Describe(Destination destination)
	{
		return $"You've arrived at {destination.Name}, which has {destination.Inhabitants} inhabitants.";
	}

	public static string Describe(TravelMethod travelMethod)
	{
		string description = travelMethod switch
		{
			TravelMethod.Walking => "by walking",
			TravelMethod.Horseback => "on horseback",
			_ => throw new ArgumentOutOfRangeException(nameof(travelMethod)),
		};
		return $"You're traveling to your destination {description}.";
	}

	public static string Describe(
			Character character,
			Destination destination,
			TravelMethod travelMethod = TravelMethod.Walking
		)
	{
		return $"{Describe(character)} {Describe(travelMethod)} {Describe(destination)}";
	}
}

internal class Character
{
	public string Class { get; set; }
	public int Level { get; set; }
	public int HitPoints { get; set; }
}

internal class Destination
{
	public string Name { get; set; }
	public int Inhabitants { get; set; }
}

internal enum TravelMethod
{
	Walking,
	Horseback
}
