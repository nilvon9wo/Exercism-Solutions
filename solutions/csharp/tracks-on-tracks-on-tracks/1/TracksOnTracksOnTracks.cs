using System.Collections.Generic;
using System.Linq;

public static class Languages
{
	private static readonly List<string> _defaultList = new()
		{
			"C#",
			"Clojure",
			"Elm"
		};

	public static List<string> NewList()
	{
		return new();
	}

	public static List<string> GetExistingLanguages()
	{
		return _defaultList;
	}

	public static List<string> AddLanguage(List<string> languages, string language)
	{
		languages.Add(language);
		return languages;
	}

	public static int CountLanguages(List<string> languages)
	{
		return languages.Count;
	}

	public static bool HasLanguage(List<string> languages, string language)
	{
		return languages.Contains(language);
	}

	public static List<string> ReverseList(List<string> languages)
	{
		languages.Reverse();
		return languages;
	}

	public static bool IsExciting(List<string> languages)
	{
		return languages.Any() && languages.Count < 4
			&& languages.Contains("C#");
	}

	public static List<string> RemoveLanguage(List<string> languages, string language)
	{
		_ = languages.Remove(language);
		return languages;
	}

	public static bool IsUnique(List<string> languages)
	{
		HashSet<string> uniqueLanguages = languages.ToHashSet();
		return languages.Count == uniqueLanguages.Count();
	}
}
