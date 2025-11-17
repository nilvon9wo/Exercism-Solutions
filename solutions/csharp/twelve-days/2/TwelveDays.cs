using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;
using System.Reflection;
using System.Text.RegularExpressions;

// ReSharper disable once CheckNamespace
public static class TwelveDays
{
	public static string Recite(int verseNumber)
		=> VerseProvider.Get(verseNumber);

	public static string Recite(int startVerse, int endVerse)
		=> string.Join(
			"\n",
			Enumerable.Range(startVerse, endVerse - startVerse + 1)
				.Select(verseNumber => $"{Recite(verseNumber)}")
		);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal enum GiftType
{
	[Description("{placeholder} in a Pear Tree")]
	[Plural("Partridges")]
	Partridge,

	[Description("Turtle {placeholder}")]
	[Plural("Doves")]
	Dove,

	[Description("French {placeholder}")]
	[Plural("Hens")]
	Hen,

	[Description("Calling {placeholder}")]
	[Plural("Birds")]
	Bird,

	[Description("Gold {placeholder}")]
	[Plural("Rings")]
	Ring,

	[Description("{placeholder}-a-Laying")]
	[Plural("Geese")]
	Goose,

	[Description("{placeholder}-a-Swimming")]
	[Plural("Swans")]
	Swan,

	[Description("{placeholder}-a-Milking")]
	[Plural("Maids")]
	Maid,

	[Description("{placeholder} Dancing")]
	[Plural("Ladies")]
	Lady,

	[Description("{placeholder}-a-Leaping")]
	[Plural("Lords")]
	Lord,

	[Description("{placeholder} Piping")]
	[Plural("Pipers")]
	Piper,

	[Description("{placeholder} Drumming")]
	[Plural("Drummers")]
	Drummer,
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class GiftTypeExtensions
{
	private static readonly Dictionary<GiftType, string> _descriptionByGift
		= EnumUtilities.ToAttributeValueByEnum<GiftType, DescriptionAttribute, string>();

	private static readonly Dictionary<GiftType, string> _pluralByGift
		= EnumUtilities.ToAttributeValueByEnum<GiftType, PluralAttribute, string>();

	private static string GetDescription(this GiftType type)
		=> _descriptionByGift[type];

	private static string GetDescription(this GiftType type, bool isSingular)
	{
		string giftWord = isSingular
			? type.ToString()
			: type.GetPlural();
		return type.GetDescription()
			.Replace("{placeholder}", giftWord, System.StringComparison.InvariantCulture);
	}

	private static string GetPlural(this GiftType type)
		=> _pluralByGift[type];

	internal static string ToPhrase(this GiftType type, int currentGiftNumber)
	{
		bool isSingular = currentGiftNumber == 1;
		string giftDescription = type.GetDescription(isSingular);
		string phrase = !isSingular
			? $"{currentGiftNumber.ToWord()} {giftDescription}"
			: giftDescription.PrependIndefiniteArticle();

		if (currentGiftNumber > 1)
		{
			phrase += ",";
		}

		return phrase;
	}
}

//=======================================================================

[AttributeUsage(AttributeTargets.All)]
// ReSharper disable once CheckNamespace
internal sealed class PluralAttribute : Attribute
{
	public PluralAttribute(string word)
		=> Word = word;

	public string Word { get; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class IntegerExtensions
{
	private static readonly Dictionary<int, Number> _numberByValues = new();

	private static Number ToNumber(this int value)
	{
		if (_numberByValues.TryGetValue(value, out Number result))
		{
			return result;
		}

		if (!Enum.IsDefined(typeof(Number), value))
		{
			throw new ArgumentOutOfRangeException(nameof(value), "Value is not a valid number.");
		}

		result = (Number)value;
		_numberByValues[value] = result;
		return result;
	}

	internal static string ToWord(this int value)
		=> value.ToNumber()
			.ToLowercaseString();

	internal static string ToOrdinal(this int value)
		=> value.ToNumber()
			.ToOrdinal();
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal enum Number
{
	[Ordinal("first")]
	One = 1,

	[Ordinal("second")]
	Two = 2,

	[Ordinal("third")]
	Three = 3,

	[Ordinal("fourth")]
	Four = 4,

	[Ordinal("fifth")]
	Five = 5,

	[Ordinal("sixth")]
	Six = 6,

	[Ordinal("seventh")]
	Seven = 7,

	[Ordinal("eighth")]
	Eight = 8,

	[Ordinal("ninth")]
	Nine = 9,

	[Ordinal("tenth")]
	Ten = 10,

	[Ordinal("eleventh")]
	Eleven = 11,

	[Ordinal("twelfth")]
	Twelve = 12,
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class NumberExtensions
{
	private static readonly Dictionary<Number, string> _ordinalByNumber
		= EnumUtilities.ToAttributeValueByEnum<Number, OrdinalAttribute, string>();

	public static string ToOrdinal(this Number value)
		=> _ordinalByNumber[value];

	[SuppressMessage("Globalization", "CA1308:Normalize strings to uppercase", Justification = "We need lower.")]
	public static string ToLowercaseString(this Number number)
		=> number.ToString()
			.ToLower(CultureInfo.InvariantCulture);
}

//=======================================================================

// ReSharper disable once CheckNamespace
[AttributeUsage(AttributeTargets.All)]
internal sealed class OrdinalAttribute : Attribute
{
	public OrdinalAttribute(string value)
		=> Value = value;

	public string Value { get; private init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class VerseProvider
{
	private static readonly Dictionary<int, GiftType> _giftByVerseNumber = new()
	{
		{ 1, GiftType.Partridge },
		{ 2, GiftType.Dove },
		{ 3, GiftType.Hen },
		{ 4, GiftType.Bird },
		{ 5, GiftType.Ring },
		{ 6, GiftType.Goose },
		{ 7, GiftType.Swan },
		{ 8, GiftType.Maid },
		{ 9, GiftType.Lady },
		{ 10, GiftType.Lord },
		{ 11, GiftType.Piper },
		{ 12, GiftType.Drummer },
	};

	internal static string Get(int verseNumber)
	{
		if (!_giftByVerseNumber.TryGetValue(verseNumber, out _))
		{
			throw new ArgumentException("Invalid verse number");
		}

		string gifts = GetGifts(verseNumber);

		if (verseNumber > 1)
		{
			int lastCommaIndex = gifts.LastIndexOf(',');
			gifts = lastCommaIndex >= 0
				? gifts.Remove(lastCommaIndex, 1)
					.Insert(lastCommaIndex, ", and")
				: "and " + gifts;
		}

		return $"On the {verseNumber.ToOrdinal()} day of Christmas my true love gave to me: {gifts}.";
	}

	private static string GetGifts(int verseNumber)
	{
		IEnumerable<string> giftDescriptions = Enumerable.Range(1, verseNumber)
			.Reverse()
			.Select(
				currentGiftNumber =>
				{
					GiftType giftType = _giftByVerseNumber[currentGiftNumber];
					return giftType.ToPhrase(currentGiftNumber);
				}
			);

		return string.Join(" ", giftDescriptions);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class AttributeExtensions
{
	public static TValue GetValue<TAttribute, TValue>(this TAttribute attribute)
		where TAttribute : Attribute
		where TValue : notnull
	{
		_ = attribute ?? throw new ArgumentNullException(nameof(attribute));
		System.Reflection.PropertyInfo[] properties = attribute.GetType()
			.GetProperties()
			.Where(property => property.PropertyType == typeof(TValue))
			.ToArray();

		return properties.Length switch
		{
			1
				=> (TValue)properties[0]
					.GetValue(attribute)!,

			> 1
				=> throw new InvalidOperationException(
					"Attribute value has multiple attributes of the specified type."
				),

			_
				=> throw new InvalidOperationException(
					"Attribute value does not have an attribute of the specified type."
				),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public static class EnumExtensions
{
	public static T GetAttributeValue<T>(this Enum enumValue) where T : Attribute
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		FieldInfo? fieldInfo = enumValue.GetType()
			.GetField(enumValue.ToString());
		T[] attributes = (T[])fieldInfo
			?.GetCustomAttributes(typeof(T), false)!;

		return attributes.Length switch
		{
			1
				=> attributes[0],

			> 1
				=> throw new InvalidOperationException("Enum value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Enum value does not have an attribute of the specified type."),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class EnumUtilities
{
	public static Dictionary<TEnum, TKey> ToAttributeValueByEnum<TEnum, TAttribute, TKey>()
		where TEnum : Enum
		where TAttribute : Attribute
		where TKey : notnull
	{
		Array enumValues = Enum.GetValues(typeof(TEnum));
		IEnumerable<TEnum> enumerableEnum = enumValues
			.Cast<TEnum>();
		return enumerableEnum
			.ToDictionary(
				enumValue
					=> enumValue,
				enumValue
					=> enumValue.GetAttributeValue<TAttribute>()
						.GetValue<TAttribute, TKey>()
			);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class StringExtensions
{
	private static readonly Regex _vowelRegex = new("^[aeiou]", RegexOptions.Compiled | RegexOptions.IgnoreCase);

	private static bool StartsWithVowel(this string input)
		=> !string.IsNullOrWhiteSpace(input) && _vowelRegex.IsMatch(input);

	internal static string PrependIndefiniteArticle(this string input)
	{
		bool startsWithVowel = input.StartsWithVowel();
		string article = startsWithVowel
			? "an"
			: "a";
		return $"{article} {input}";
	}
}

//=======================================================================