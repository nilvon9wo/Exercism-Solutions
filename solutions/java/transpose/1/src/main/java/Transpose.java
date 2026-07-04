import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Transpose {

    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");
    public String transpose(String input) {
        if (input.isEmpty()) {
            return "";
        }

        String[] rows = NEW_LINE_PATTERN.split(input, -1);
        int maximumWidth = this.getMaximumWidth(rows);
        return this.buildTransposedOutput(maximumWidth, rows);
    }

    private int getMaximumWidth(final String[] rows) {
        return Arrays.stream(rows)
                     .mapToInt(String::length)
                     .max()
                     .orElse(0);
    }

    private String buildTransposedOutput(final int maximumWidth, final String[] rows) {
        return IntStream.range(0, maximumWidth)
                        .mapToObj(column -> this.transposeColumn(rows, column))
                        .collect(Collectors.joining("\n"));
    }

    private String transposeColumn(String[] rows, int column) {
        int lastRow = this.findLastRowContainingColumn(rows, column);
        return IntStream.rangeClosed(0, lastRow)
                        .mapToObj(row -> this.characterAt(rows[row], column))
                        .collect(Collectors.joining());
    }

    private int findLastRowContainingColumn(String[] rows, int column) {
        return IntStream.range(0, rows.length)
                        .filter(row -> column < rows[row].length())
                        .max()
                        .orElse(-1);
    }

    private String characterAt(String row, int column) {
        return (column < row.length())
            ? String.valueOf(row.charAt(column))
            : " ";
    }
}