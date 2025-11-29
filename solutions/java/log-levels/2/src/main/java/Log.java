import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log {
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

	private final String level;
	private final String message;
	public Log(String logLine) {
		Matcher m = PATTERN.matcher(logLine);
		if (!m.matches()) {
			throw new IllegalArgumentException("Invalid log line: " + logLine);
		}

		this.level = m.group("level")
				             .toLowerCase();
		this.message = m.group("message");
	}

	public String getLevel() {
		return this.level;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return this.message + " (" + this.level + ")";
	}
}
