import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class ResistorColorDuo {
	private static final Map<String, Integer> valueByResistorColors = Map.of(
			"black", 0,
			"brown", 1,
			"red", 2,
			"orange", 3,
			"yellow", 4,
			"green", 5,
			"blue", 6,
			"violet", 7,
			"grey", 8,
			"white", 9
	);

    int value(String[] colors) {
	    String digits = Arrays.stream(colors)
			                    .limit(2)
			                    .map(this.getResistorValue())
			                    .collect(Collectors.joining());
	    return Integer.parseInt(digits);
    }

	private Function<String, String> getResistorValue() {
		return color -> String.valueOf(valueByResistorColors.get(color));
	}
}
