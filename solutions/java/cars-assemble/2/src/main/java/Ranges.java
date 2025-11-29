public final class Ranges {
	private Ranges() {}

	public static Speed speed(int value) {
		return new Speed(value);
	}

	public static SpeedRange speedRange(int start, int end) {
		return new SpeedRange(start, end);
	}
}
