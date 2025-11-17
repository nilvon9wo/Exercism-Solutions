using System;
using System.Collections.Generic;
using System.ComponentModel;

namespace LogsLogsLogs;
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
