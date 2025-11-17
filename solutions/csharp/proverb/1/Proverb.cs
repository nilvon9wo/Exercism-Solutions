using System;
using System.Collections.Generic;
using System.Linq;

public static class Proverb
{
	public static string[] Recite(string[] subjects)
	{
		if (!subjects.Any())
		{
			return Array.Empty<string>();
		}
		else
		{
			List<string> subjectList = subjects.ToList();
			string firstSubject = subjectList.Shift();

			List<string> verses = new();
			string previousSubject = firstSubject;
			while (subjectList.Any())
			{
				string currentSubject = subjectList.Shift();
				verses.Add($"For want of a {previousSubject} the {currentSubject} was lost.");
				previousSubject = currentSubject;
			}

			verses.Add($"And all for the want of a {firstSubject}.");
			return verses.ToArray();
		}
	}
}

public static class CollectionExtensions
{
	public static T Shift<T>(this ICollection<T> haystack)
	{
		T value = haystack.FirstOrDefault();
		_ = haystack.Remove(value);
		return value;
	}
}