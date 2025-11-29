import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

public enum LogLevel {
	TRACE("TRC", 1),
	DEBUG("DBG", 2),
	INFO("INF", 4),
	WARNING("WRN", 5),
	ERROR("ERR", 6),
	FATAL("FTL", 42),
	UNKNOWN(null, 0);

	private final String code;
	private final int shortFormat;

	LogLevel(String code, int shortFormat) {
		this.code = code;
		this.shortFormat = shortFormat;
	}

	public int getShortFormat() {
		return this.shortFormat;
	}

	private static final Map<String, LogLevel> CODE_TO_ENUM =
			EnumSet.allOf(LogLevel.class)
					.stream()
					.filter(level -> level.code != null)
					.collect(Collectors.toMap(
							level -> level.code.toLowerCase(),
							level -> level
					));

	public static LogLevel fromCode(String code) {
		return code == null
				       ? UNKNOWN
				       : CODE_TO_ENUM.getOrDefault(code.toLowerCase(), UNKNOWN);

	}
}
