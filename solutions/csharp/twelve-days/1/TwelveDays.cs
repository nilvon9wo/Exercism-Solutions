using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class TwelveDays
{
	private static readonly Dictionary<int, TwelveDaysVerse> _verses = new()
	{
		{ 1, new() { Ordinal = "first", Gift = "a Partridge in a Pear Tree" } },
		{ 2, new() { Ordinal = "second", Gift = "two Turtle Doves" } },
		{ 3, new() { Ordinal = "third", Gift = "three French Hens" } },
		{ 4, new() { Ordinal = "fourth", Gift = "four Calling Birds" } },
		{ 5, new() { Ordinal = "fifth", Gift = "five Gold Rings" } },
		{ 6, new() { Ordinal = "sixth", Gift = "six Geese-a-Laying" } },
		{ 7, new() { Ordinal = "seventh", Gift = "seven Swans-a-Swimming" } },
		{ 8, new() { Ordinal = "eighth", Gift = "eight Maids-a-Milking" } },
		{ 9, new() { Ordinal = "ninth", Gift = "nine Ladies Dancing" } },
		{ 10, new() { Ordinal = "tenth", Gift = "ten Lords-a-Leaping" } },
		{ 11, new() { Ordinal = "eleventh", Gift = "eleven Pipers Piping" } },
		{ 12, new() { Ordinal = "twelfth", Gift = "twelve Drummers Drumming" } },
	};

	public static string Recite(int verseNumber)
	{
		if (!_verses.TryGetValue(verseNumber, out TwelveDaysVerse? value))
		{
			throw new ArgumentException("Invalid verse number");
		}

		string gifts = string.Join(
			", ",
			_verses
				.Where(kv => kv.Key <= verseNumber)
				.OrderByDescending(kv => kv.Key)
				.Select(kv => kv.Value.Gift)
		);

		if (verseNumber > 1)
		{
			int lastCommaIndex = gifts.LastIndexOf(',');
			gifts = lastCommaIndex >= 0
				? gifts.Remove(lastCommaIndex, 1)
					.Insert(lastCommaIndex, ", and")
				: "and " + gifts;
		}

		return $"On the {value.Ordinal} day of Christmas my true love gave to me: {gifts}.";
	}

	public static string Recite(int startVerse, int endVerse)
		=> string.Join(
			"\n",
			Enumerable.Range(startVerse, endVerse - startVerse + 1)
				.Select(verseNumber => $"{Recite(verseNumber)}")
		);
}

// ReSharper disable once CheckNamespace
internal class TwelveDaysVerse
{
	internal required string Ordinal { get; init; }
	internal required string Gift { get; init; }
}

internal enum Numbers
{
	[Ordinal("first")]
	[Gift("Partridge in a Pear Tree")]
	One = 1,

	[Ordinal("second")]
	[Gift("Turtle Doves")]
	Two = 2,

	[Ordinal("third")]
	[Gift("French Hens")]
	Three = 3,

	[Ordinal("fourth")]
	[Gift("Calling Birds")]
	Four = 4,

	[Ordinal("fifth")]
	[Gift("Gold Rings")]
	Five = 5,

	[Ordinal("sixth")]
	[Gift("Geese-a-Laying")]
	Six = 6,

	[Ordinal("seventh")]
	[Gift("Swans-a-Swimming")]
	Seven = 7,

	[Ordinal("eighth")]
	[Gift("Maids-a-Milking")]
	Eight = 8,

	[Ordinal("ninth")]
	[Gift("Ladies Dancing")]
	Nine = 9,

	[Ordinal("tenth")]
	[Gift("Lords-a-Leaping")]
	Ten = 10,

	[Ordinal("eleventh")]
	[Gift("Pipers Piping")]
	Eleven = 11,

	[Ordinal("twelfth")]
	[Gift("Drummers Drumming")]
	Twelve = 12,
}

// ReSharper disable once CheckNamespace
[AttributeUsage(AttributeTargets.All)]
internal sealed class GiftAttribute : Attribute
{
	public GiftAttribute(string value)
		=> Value = value;

	public string Value { get; private init; }
}

// ReSharper disable once CheckNamespace
[AttributeUsage(AttributeTargets.All)]
internal sealed class OrdinalAttribute : Attribute
{
	public OrdinalAttribute(string value)
		=> Value = value;

	public string Value { get; private init; }
}