using System.Text.RegularExpressions;

internal static class LogLine
{
	private static readonly Regex LogPattern = new(@"\[(\w*)\]\:\s*(.*)", RegexOptions.Compiled);

	public static string Message(string logLine)
	{
		return LogPattern.Match(logLine)
			.Groups[2].Value
			.Trim();
	}

	public static string LogLevel(string logLine)
	{
		return LogPattern.Match(logLine)
			.Groups[1].Value
			.Trim()
			.ToLowerInvariant();
	}

	public static string Reformat(string logLine)
	{
		string message = Message(logLine);
		string level = LogLevel(logLine);
		return $"{message} ({level})";
	}
}
