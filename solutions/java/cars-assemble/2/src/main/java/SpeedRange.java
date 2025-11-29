public record SpeedRange(int start, int end) implements Range<Integer> {
	public SpeedRange {
		if (start > end) {
			throw new IllegalArgumentException("start must be <= end");
		}
	}

	@Override
	public boolean contains(Integer key) {
		return key >= this.start
				       && key <= this.end;
	}
}