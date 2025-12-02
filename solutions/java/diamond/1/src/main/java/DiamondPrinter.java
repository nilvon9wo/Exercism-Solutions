import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class DiamondPrinter {

	private static final char FIRST_LETTER = 'A';
	private static final int OUTER_MULTIPLIER = 2;
	private static final int INNER_OFFSET = 1;
	private static final String SPACE = " ";

	List<String> printToList(char a) {
		int n = a - FIRST_LETTER;
		int width = OUTER_MULTIPLIER * n + 1;

		Stream<String> top = this.createTop(n, width);
		Stream<String> bottom = this.createBottom(n, width);
		return Stream.concat(top, bottom)
				       .collect(Collectors.toList());
	}

	private Stream<String> createTop(int n, int width) {
		IntStream indices = IntStream.rangeClosed(0, n);
		return this.buildPart(indices, width);
	}

	private Stream<String> createBottom(int n, int width) {
		IntStream indices = IntStream.range(0, n)
				                    .map(i -> n - 1 - i);
		return this.buildPart(indices, width);
	}

	private Stream<String> buildPart(IntStream indexStream, int width) {
		return indexStream.mapToObj(i -> this.buildLine(i, width))
				       .toList()
				       .stream();
	}

	private String buildLine(int index, int width) {
		char letter = (char) (FIRST_LETTER + index);
		String content = index == 0
				                 ? String.valueOf(letter)
				                 : letter + this.createInnerSpaces(index) + letter;
		return this.pad(content, width);
	}

	private String createInnerSpaces(int index) {
		int innerSpaceCount = OUTER_MULTIPLIER * index - INNER_OFFSET;
		return SPACE.repeat(innerSpaceCount);
	}

	private String pad(String lineContent, int width) {
		int outer = (width - lineContent.length()) / 2;
		String outerPadding = SPACE.repeat(outer);
		return outerPadding + lineContent + outerPadding;
	}
}
