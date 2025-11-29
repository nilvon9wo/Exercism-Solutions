public class LogLevels {
    
    public static String message(String logLine) {
		return new Log(logLine)
				       .getMessage();
    }

    public static String logLevel(String logLine) {
	    return new Log(logLine)
			           .getLevel();
    }

    public static String reformat(String logLine) {
	    return new Log(logLine)
			           .toString();
    }
}
