using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text.RegularExpressions;

public enum LogLevel
{
	[Description("TRC")]
	Trace = 1,

	[Description("DBG")]
	Debug = 2,

	[Description("INF")]
	Info = 4,

	[Description("WRN")]
	Warning = 5,

	[Description("ERR")]
	Error = 6,

	[Description("FTL")]
	Fatal = 42,

	Unknown = 0
}

public static class LogLevelExtensions
{
	private static Dictionary<string, LogLevel> _logLevelByString;
	public static Dictionary<string, LogLevel> LogLevelByString
	{
		get
		{
			if (_logLevelByString == null)
			{
				_logLevelByString = new();
				foreach (LogLevel level in Enum.GetValues(typeof(LogLevel)))
				{
					string levelString = ToDescriptionString(level);
					_logLevelByString[levelString] = level;
				}
			}

			return _logLevelByString;
		}
	}

	public static string ToDescriptionString(this LogLevel value)
	{
		DescriptionAttribute[] attributes = (DescriptionAttribute[])value
		   .GetType()
		   .GetField(value.ToString())
		   .GetCustomAttributes(typeof(DescriptionAttribute), false);

		return attributes.Length > 0
			? attributes[0].Description
			: string.Empty;
	}

	public static LogLevel ToLogLevel(this string value)
	{
		return LogLevelByString.TryGetValue(value, out LogLevel level)
			? level
			: LogLevel.Unknown;
	}
}

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
