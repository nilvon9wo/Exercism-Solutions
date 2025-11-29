import java.util.*;

public class RangeMap<K, V> {
	private final List<Entry<K, V>> entries = new ArrayList<>();

	private RangeMap(List<Entry<K, V>> entries) {
		this.entries.addAll(entries);
	}

	public void put(Range<K> range, V value) {
		entries.add(new Entry<>(range, value));
	}

	public V get(K key) {
		return entries.stream()
				       .filter(e -> e.range().contains(key))
				       .map(Entry::value)
				       .findFirst()
				       .orElse(null);
	}

	@SafeVarargs
	public static <K, V> RangeMap<K, V> of(Pair<K, V>... pairs) {
		List<Entry<K, V>> entryList
				= Arrays.stream(pairs)
	              .map(pair -> new Entry<>(pair.getRange(), pair.getValue()))
	              .toList();

		return new RangeMap<>(entryList);
	}
}
