import java.util.Arrays;

enum ResistorColor {
	BLACK(0),
	BROWN(1, 10, ""),
	RED(2, 0.1, "kilo"),
	ORANGE(3, 1, "kilo"),
	YELLOW(4, 10, "kilo"),
	GREEN(5),
	BLUE(6, 1, "mega"),
	VIOLET(7),
	GREY(8),
	WHITE(9, 1, "giga");

	private final int value;
	private final double displayMultiplier;
	private final String displayPrefix;

	ResistorColor(int value, double displayMultiplier, String displayPrefix) {
		this.value = value;
		this.displayMultiplier = displayMultiplier;
		this.displayPrefix = displayPrefix;
	}

	ResistorColor(int value) {
		this(value, 1, "");
	}

	public int getValue() {
		return value;
	}

	public double getDisplayMultiplier() {
		return displayMultiplier;
	}

	public String getDisplayPrefix() {
		return displayPrefix;
	}

	public static ResistorColor from(String colorName) {
		return Arrays.stream(values())
				       .filter(color -> color.name().equalsIgnoreCase(colorName))
				       .findFirst()
				       .orElseThrow(() -> new IllegalArgumentException("Unknown color: " + colorName));
	}
}

