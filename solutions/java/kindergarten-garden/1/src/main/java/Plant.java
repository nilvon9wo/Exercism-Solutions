import java.util.Arrays;

enum Plant {
	VIOLETS('V'),
	RADISHES('R'),
	CLOVER('C'),
	GRASS('G');

	private final char code;
	Plant(char code) {
		this.code = code;
	}

	static Plant fromCode(char code) {
		return Arrays.stream(values())
				       .filter(plant -> plant.code == code)
				       .findFirst()
				       .orElseThrow(() -> new IllegalArgumentException("Unknown plant code: " + code));
	}
}