import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record MultiKeyMap<K, V>(Map<K, V> valueByKeys) {

	public static <K, V> MultiKeyMap<K, V> fromMapOfSets(Map<Set<K>, V> mapOfSets) {
		Map<K, V> flatMap = mapOfSets.entrySet()
				                    .stream()
				                    .flatMap(createPairStream())
				                    .collect(getMap());
		return new MultiKeyMap<>(flatMap);
	}

	private static <K, V> Function<Map.Entry<Set<K>, V>, Stream<Map.Entry<K, V>>> createPairStream() {
		return entry -> entry.getKey()
				                .stream()
				                .map(key -> Map.entry(key, entry.getValue()));
	}

	private static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> getMap() {
		return Collectors.toMap(
				Map.Entry::getKey,
				Map.Entry::getValue
		);
	}

	public V get(K key) {
		return valueByKeys.get(key);
	}
}
