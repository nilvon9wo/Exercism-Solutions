using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text.RegularExpressions;

// ReSharper disable once CheckNamespace
public static class WordCount
{
	private static readonly Regex _wordPattern = new(@"[^\w']+", RegexOptions.Compiled);

	public static IDictionary<string, int> CountWords(string phrase)
		=> _wordPattern.Split(phrase)
			.Where(word => !string.IsNullOrWhiteSpace(word))
			.Select(Clean)
			.GroupBy(word => word)
			.Where(x => !string.IsNullOrEmpty(x.Key))
			.ToDictionary(group => group.Key, group => group.Count());

	private static string Clean(string word)
		=> word.Trim('\'')
			.ToLower(CultureInfo.CurrentCulture);
}