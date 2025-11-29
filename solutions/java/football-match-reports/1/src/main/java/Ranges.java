public final class Ranges {
	private Ranges() {}

	public static IntegerValue number(int value) {
		return new IntegerValue(value);
	}

	public static IntegerRange numberRange(int start, int end) {
		return new IntegerRange(start, end);
	}
}
