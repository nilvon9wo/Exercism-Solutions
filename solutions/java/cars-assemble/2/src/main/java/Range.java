public interface Range<K> {
	boolean contains(K key);

	default <V> Pair<K, V> to(V value) {
		return Pair.of(this, value);
	}
}
