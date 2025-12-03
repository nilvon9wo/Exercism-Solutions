import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;

class Matrix {
	private final String matrixAsString;

	Matrix(String matrixAsString) {
		this.matrixAsString = matrixAsString;
	}

	private Map<Coordinate, Integer> valueByCoordinates;
	private Map<Coordinate, Integer> getValueByCoordinates() {
		if (this.valueByCoordinates == null) {
			this.valueByCoordinates = this.buildValueMap();
		}
		return this.valueByCoordinates;
	}

	private Map<Coordinate, Integer> buildValueMap() {
		String[] lines = matrixAsString.split("\n");
		return IntStream.range(0, lines.length)
				       .mapToObj(i -> rowEntries(i + 1, lines[i]))
				       .flatMap(stream -> stream)
				       .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	int[] getRow(int rowNumber) {
		return this.extract(Axis.ROW, rowNumber);
	}

	int[] getColumn(int columnNumber) {
		return this.extract(Axis.COLUMN, columnNumber);
	}

	private int[] extract(Axis axis, int index) {
		return this.getValueByCoordinates().entrySet().stream()
				       .filter(this.isMatch(axis, index))
				       .sorted(Map.Entry.comparingByKey(axis.comparator()))
				       .mapToInt(Map.Entry::getValue)
				       .toArray();
	}

	private Predicate<Map.Entry<Coordinate, Integer>> isMatch(Axis axis, int index) {
		return entry
				       -> axis.primaryIndexOf(entry.getKey()) == index;
	}

	private Stream<Map.Entry<Coordinate, Integer>> rowEntries(int rowIndex, String line) {
		String[] tokens = line.trim()
				                  .split("\\s+");
		return IntStream.range(0, tokens.length)
				       .mapToObj(column -> this.toCellEntry(rowIndex, column, tokens));
	}

	private Map.Entry<Coordinate, Integer> toCellEntry(int rowIndex, int column, String[] tokens) {
		return Map.entry(
				new Coordinate(rowIndex, column + 1),
				Integer.parseInt(tokens[column])
		);
	}
}
