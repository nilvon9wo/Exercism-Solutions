import java.util.OptionalInt;

public class Validator {
    public void validate(JigsawInfo input) {
        OptionalInt inputRows = input.getRows();
        Integer rowsHint = inputRows.isPresent()
                        ? inputRows.getAsInt()
                        : null;
        OptionalInt inputColumns = input.getColumns();
        Integer columnsHint = inputColumns.isPresent()
                           ? inputColumns.getAsInt()
                           : null;

        String formatHint = input.getFormat().orElse(null);
        if (rowsHint != null && columnsHint != null) {

            if ("square".equals(formatHint) && !rowsHint.equals(columnsHint)) {
                throw new IllegalArgumentException("Contradictory data");
            }

            if ("portrait".equals(formatHint) && columnsHint >= rowsHint) {
                throw new IllegalArgumentException("Contradictory data");
            }

            if ("landscape".equals(formatHint) && columnsHint <= rowsHint) {
                throw new IllegalArgumentException("Contradictory data");
            }
        }
    }
}
