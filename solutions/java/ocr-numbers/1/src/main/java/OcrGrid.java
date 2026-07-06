import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class OcrGrid {
    private static final int ROW_HEIGHT = 4;
    private static final int COLUMN_WIDTH = 3;

    List<String[]> extractDigitCells(List<String> input) {
        return IntStream.iterate(0, row -> row < input.size(), row -> row + ROW_HEIGHT)
                        .mapToObj(row -> input.subList(row, row + ROW_HEIGHT))
                        .flatMap(block -> extractRow(block).stream())
                        .collect(Collectors.toList());
    }

    private List<String[]> extractRow(List<String> block) {
        int width = block.get(0).length();
        int digits = width / COLUMN_WIDTH;
        return IntStream.range(0, digits)
                        .mapToObj(digit -> extractDigit(block, digit))
                        .collect(Collectors.toList());
    }

    private String[] extractDigit(List<String> block, int digitIndex) {
        int columnStart = digitIndex * COLUMN_WIDTH;
        return IntStream.range(0, 4)
                        .mapToObj(row -> block.get(row)
                                             .substring(columnStart, columnStart + COLUMN_WIDTH))
                        .toArray(String[]::new);
    }
}