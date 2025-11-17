using System.Text.RegularExpressions;

public static class LogAnalysis
{
	private static readonly Regex LogPattern = new(@"\[(\w*)\]\:\s*(.*)", RegexOptions.Compiled);

	public static string SubstringAfter(this string haystack, string needle)
	{
		int firstOccurance = haystack.IndexOf(needle);
		int firstCharacterAfter = firstOccurance + needle.Length;
		return haystack[firstCharacterAfter..];
	}

	public static string SubstringBetween(this string haystack, string opener, string closer)
	{
		int startIndex = haystack.IndexOf(opener) + opener.Length;
		int endIndex = haystack.LastIndexOf(closer);
		return haystack[startIndex..endIndex];
	}

	public static string Message(this string value) =>
		value.SplitLog()
			.Message;

	public static string LogLevel(this string value) =>
		value.SplitLog()
			.LogLevel;

	private static LogStructure SplitLog(this string value)
	{
		Match match = LogPattern.Match(value);
		return match.Success
			? (match.Groups[1].Value, match.Groups[2].Value)
			: ("", "");

	}
}

internal record struct LogStructure(string LogLevel, string Message)
{
	public static implicit operator (string LogLevel, string Message)(LogStructure value) =>
		(value.LogLevel, value.Message);

	public static implicit operator LogStructure((string LogLevel, string Message) value) =>
		new(value.LogLevel, value.Message);
}