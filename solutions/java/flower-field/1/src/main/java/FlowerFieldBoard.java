import java.util.*;
import java.util.stream.IntStream;

public final class FlowerFieldBoard {
    private final List<String> boardRows;
    private final CellCharacterMapFactory  mapFactory;
    FlowerFieldBoard(List<String> boardRows, CellCharacterMapFactory  mapFactory) {
        this.boardRows = boardRows;
        this.mapFactory = mapFactory;
    }

    FlowerFieldBoard(List<String> boardRows) {
        this(boardRows, new CellCharacterMapFactory());
    }

    List<String> withNumbers() {
        if (this.boardRows == null) {
            throw new NullPointerException("input");
        }

        if (this.boardRows.isEmpty()) {
            return List.of(new String[0]);
        }

        if (this.boardRows.size() == 1 && this.isEmpty()) {
            return List.of("");
        }

        final CellCharacterMap cellCharacterMap = this.mapFactory.createFrom(this.boardRows);
        final String[] outputRows = this.buildOutput(cellCharacterMap);
        return Arrays.stream(outputRows)
                     .toList();
    }

    private boolean isEmpty() {
        return this.boardRows.getFirst()
                             .isEmpty();
    }

    private String[] buildOutput(CellCharacterMap cellCharacterMap) {
        return IntStream.range(0, boardRows.size())
                        .mapToObj(i -> this.buildRowString(cellCharacterMap, i))
                        .toArray(String[]::new);
    }

    private String buildRowString(final CellCharacterMap cellCharacterMap, final int rowIndex) {
        String row = boardRows.get(rowIndex);
        return IntStream.range(0, row.length())
                        .mapToObj(colIndex -> cellCharacterMap.getCharacterAtCell(rowIndex, colIndex))
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();
    }
}