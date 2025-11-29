public class Speed implements Range<Integer> {
	private final int value;

	public Speed(int value) {
		this.value = value;
	}

	@Override
	public boolean contains(Integer key) {
		return key == this.value;
	}

	public <V> Pair<Integer, V> to(V percentage) {
		return Pair.of(this, percentage);
	}
}