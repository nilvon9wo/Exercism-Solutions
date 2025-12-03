import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Proverb {
	private final String[] words;
	Proverb(String[] words) {
		this.words = words;
	}

	private LinkedHashMap<String, String> lossByWant;
	private LinkedHashMap<String, String> getLossByWant() {
		if (this.lossByWant == null) {
			this.lossByWant = buildLossMap(words);
		}
		return this.lossByWant;
	}

	String recite() {
		if (words.length == 0) return "";
		Stream<String> consequences = IntStream.range(0, this.getLossByWant().size())
				                              .mapToObj(this::consequenceLineAt);
		return Stream.concat(consequences, Stream.of(this.finalLine()))
				       .collect(Collectors.joining("\n"));
	}

	// ---------- helpers ----------

	private LinkedHashMap<String, String> buildLossMap(String[] words) {
		return IntStream.range(0, Math.max(0, words.length - 1))
				       .boxed()
				       .collect(this.buildLossMapCollector(words));
	}

	private Collector<Integer, ?, LinkedHashMap<String, String>> buildLossMapCollector(String[] words) {
		return Collectors.toMap(
				i -> words[i],
				i -> words[i + 1],
				(a, b) -> a,
				LinkedHashMap::new
		);
	}

	private String consequenceLineAt(int index) {
		Set<Map.Entry<String, String>> entriesSet = this.getLossByWant()
				                                     .entrySet();
		List<Map.Entry<String, String>> entries = new ArrayList<>(entriesSet);
		Map.Entry<String, String> entry = entries.get(index);
		return "For want of a " + entry.getKey()
				       + " the " + entry.getValue() + " was lost.";
	}

	private String finalLine() {
		return "And all for the want of a " + words[0] + ".";
	}
}
