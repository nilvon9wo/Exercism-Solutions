public record Pair<K, V>(Range<K> range, V value) {

	public Range<K> getRange() {
		return this.range;
	}

	public V getValue() {
		return this.value;
	}

	public static <K, V> Pair<K, V> of(Range<K> range, V value) {
		return new Pair<>(range, value);
	}
}
