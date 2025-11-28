public record Speed(int value) implements Range<Integer> {

	@Override
	public boolean contains(Integer key) {
		return key == this.value;
	}
}