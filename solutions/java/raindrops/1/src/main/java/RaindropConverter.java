import java.util.LinkedHashMap;
import java.util.Map;

class RaindropConverter {
	private static final Map<Integer, String> soundByDivisor = new LinkedHashMap<>() {{
		put(3, "Pling");
		put(5, "Plang");
		put(7, "Plong");
	}};

	String convert(int number) {
		String sound = this.createSound(number);
		return !sound.isBlank()
				       ? sound
				       : String.valueOf(number);
	}

	private String createSound(int number) {
		return soundByDivisor.entrySet()
				               .stream()
				               .filter(entry -> this.isFactor(number, entry))
				               .map(Map.Entry::getValue)
				               .reduce("", String::concat);
	}

	private boolean isFactor(int number, Map.Entry<Integer, String> entry) {
		Integer divisor = entry.getKey();
		return number % divisor == 0;
	}
}
