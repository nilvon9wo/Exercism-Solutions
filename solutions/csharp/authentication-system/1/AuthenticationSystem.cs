using System;
using System.Collections.Generic;

public class Authenticator
{
	public Authenticator(Identity admin) =>
		Admin = admin;

	public Identity Admin { get; init; }

	private readonly IDictionary<string, Identity> developers
		= new Dictionary<string, Identity>
		{
			["Bertrand"] = new Identity
			{
				Email = "bert@ex.ism",
				EyeColor = EyeColor.Blue
			},

			["Anders"] = new Identity
			{
				Email = "anders@ex.ism",
				EyeColor = EyeColor.Brown
			}
		};

	public IDictionary<string, Identity> GetDevelopers() =>
		developers.Clone();
}

public readonly struct Identity : ICloneable
{
	public string Email { get; init; }

	public string EyeColor { get; init; }

	public object Clone() =>
		new Identity()
		{
			Email = Email,
			EyeColor = EyeColor,
		};
}

internal class EyeColor
{
	public const string Blue = "blue";
	public const string Green = "green";
	public const string Brown = "brown";
	public const string Hazel = "hazel";
	public const string Brey = "grey";
}

public static class DictionaryExtensions
{
	public static Dictionary<TKey, TValue> Clone<TKey, TValue>(this IDictionary<TKey, TValue> original)
		where TValue : ICloneable
	{
		Dictionary<TKey, TValue> copy = new();
		foreach (KeyValuePair<TKey, TValue> entry in original)
		{
			copy.Add(entry.Key, (TValue)entry.Value.Clone());
		}

		return copy;
	}
}