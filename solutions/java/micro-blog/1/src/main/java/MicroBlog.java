public class MicroBlog {
	private static final int MAX_LENGTH = 5;

	public String truncate(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		int[] codePoints = input.codePoints()
				                   .toArray();
		int lengthToTake = Math.min(MAX_LENGTH, codePoints.length);
		return new String(codePoints, 0, lengthToTake);
	}
}
