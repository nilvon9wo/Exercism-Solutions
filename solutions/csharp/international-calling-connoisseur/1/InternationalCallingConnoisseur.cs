using System;
using System.Collections.Generic;
using System.Linq;

public static class DialingCodes
{
	private static readonly Dictionary<int, string> _defaultCountryCode = new()
	{
		{ 1,  "United States of America" },
		{ 55, "Brazil" },
		{ 91, "India" }
	};

	public static Dictionary<int, string> GetEmptyDictionary()
	{
		return new();
	}

	public static Dictionary<int, string> GetExistingDictionary()
	{
		return CloneDictionaryCloningValues(_defaultCountryCode);
	}

	public static Dictionary<int, string> AddCountryToEmptyDictionary(int countryCode, string countryName)
	{
		Dictionary<int, string> dictionary = GetEmptyDictionary();
		return AddCountryToExistingDictionary(dictionary, countryCode, countryName);
	}

	public static Dictionary<int, string> AddCountryToExistingDictionary(
		Dictionary<int, string> existingDictionary, int countryCode, string countryName)
	{
		existingDictionary.Add(countryCode, countryName);
		return existingDictionary;
	}

	public static string GetCountryNameFromDictionary(
		Dictionary<int, string> existingDictionary, int countryCode)
	{
		return existingDictionary.TryGetValue(countryCode, out string countryName)
			? countryName
			: string.Empty;
	}

	public static bool CheckCodeExists(Dictionary<int, string> existingDictionary, int countryCode)
	{
		return existingDictionary.ContainsKey(countryCode);
	}

	public static Dictionary<int, string> UpdateDictionary(
		Dictionary<int, string> existingDictionary, int countryCode, string countryName)
	{
		if (existingDictionary.ContainsKey(countryCode))
		{
			existingDictionary[countryCode] = countryName;
		}

		return existingDictionary;
	}

	public static Dictionary<int, string> RemoveCountryFromDictionary(
			Dictionary<int, string> existingDictionary,
			int countryCode
		)
	{
		_ = existingDictionary.Remove(countryCode);
		return existingDictionary;
	}

	public static string FindLongestCountryName(Dictionary<int, string> existingDictionary)
	{
		IOrderedEnumerable<string> orderedCountyNames = existingDictionary.Values
					.OrderBy(x => x.Length);

		return orderedCountyNames.Any()
			? orderedCountyNames.Last()
			: string.Empty;
	}

	private static Dictionary<TKey, TValue> CloneDictionaryCloningValues<TKey, TValue>(Dictionary<TKey, TValue> original)
		where TValue : ICloneable
	{
		Dictionary<TKey, TValue> returnDictionary = new(original.Count,
																original.Comparer);
		foreach (KeyValuePair<TKey, TValue> entry in original)
		{
			returnDictionary.Add(entry.Key, (TValue)entry.Value.Clone());
		}

		return returnDictionary;
	}
}