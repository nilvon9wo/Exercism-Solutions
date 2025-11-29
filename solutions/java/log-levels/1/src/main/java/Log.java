import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log {
	private static final Pattern PATTERN =
			Pattern.compile("^\\s*\\[\\s*(?<level>[A-Za-z]+)\\s*]\\s*:\\s*(?<message>.*?)\\s*$");

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
