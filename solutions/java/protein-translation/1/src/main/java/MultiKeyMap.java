import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record MultiKeyMap<V>(Map<String, V> valueByKeys) {

	public static <V> MultiKeyMap<V> fromMapOfSets(Map<Set<String>, V> mapOfSets) {
		Map<String, V> flatMap = mapOfSets.entrySet()
				                         .stream()
				                         .flatMap(createPairStream())
				                         .collect(getMap());
		return new MultiKeyMap<>(flatMap);
	}

	private static <V> Function<Map.Entry<Set<String>, V>, Stream<Map.Entry<String, V>>> createPairStream() {
		return entry -> entry.getKey()
				                .stream()
				                .map(key -> Map.entry(key, entry.getValue()));
	}

	private static <V> Collector<Map.Entry<String, V>, ?, Map<String, V>> getMap() {
		return Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue
		);
	}

	public V get(String key) {
		return valueByKeys.get(key);
	}
}
