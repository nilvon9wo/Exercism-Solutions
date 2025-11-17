using LogsLogsLogs;

using System.Text.RegularExpressions;

internal static class LogLine
{
	private static readonly Regex LogPattern = new(@"\[(\w*)\]\:\s*(.*)", RegexOptions.Compiled);

	public static LogLevel ParseLogLevel(string logLine)
	{
		Match match = LogPattern.Match(logLine);
		if (match.Success)
		{
			return match.Groups[1].Value
				.ToLogLevel();

		}
		else
		{
			return LogLevel.Unknown;
		}
	}

	public static string OutputForShortLog(LogLevel logLevel, string message)
	{
		return $"{(int)logLevel}:{message}";
	}
}
