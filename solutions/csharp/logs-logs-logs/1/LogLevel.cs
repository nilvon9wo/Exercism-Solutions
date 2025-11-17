using System.ComponentModel;

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