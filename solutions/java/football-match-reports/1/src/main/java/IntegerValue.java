public record IntegerValue(int value) implements Range<Integer> {

	@Override
	public boolean contains(Integer key) {
		return key == this.value;
	}
}