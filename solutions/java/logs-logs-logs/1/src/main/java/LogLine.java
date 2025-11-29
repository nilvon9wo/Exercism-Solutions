import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogLine {
	private static final Pattern PATTERN = Pattern.compile(
			"^"                     // start of line
		  + "\\s*"                  // optional leading whitespace
		  + "\\["                   // opening bracket
		  + "\\s*(?<level>[A-Za-z]+)\\s*"  // log level, optional spaces around
		  + "]"                     // closing bracket
		  + "\\s*:"                 // colon with optional spaces
		  + "\\s*(?<message>.*?)"   // message, non-greedy
		  + "\\s*$"                 // optional trailing whitespace to end of line
	);

	private final LogLevel level;
	private final String message;
	public LogLine(String logLine) {
		Matcher matcher = PATTERN.matcher(logLine);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid log line: " + logLine);
		}

		String levelCode = matcher.group("level");
		this.level = LogLevel.fromCode(levelCode);
		this.message = matcher.group("message");
	}

	public LogLevel getLogLevel() {
		return this.level;
	}

	public String getOutputForShortLog() {
		return this.level.getShortFormat() + ":" + this.message;
	}

	@Override
	public String toString() {
		return this.message + " (" + this.level + ")";
	}
}
