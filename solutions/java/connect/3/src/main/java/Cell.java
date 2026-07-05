import java.util.*;
import java.util.stream.*;

record Cell(HexCoordinates coordinates, BoardContent content) {
    private static final Map<Character, BoardContent> BOARD_CONTENT_BY_CHARACTER =
            Arrays.stream(BoardContent.values())
                  .collect(Collectors.toMap(
                          BoardContent::getInputCharacter,
                          boardContent -> boardContent
                  ));

    boolean belongsTo(Player player) {
        return content == player.boardContent;
    }

    static List<Cell> manyFrom(String rowString, int row) {
        List<Cell> result = new ArrayList<>();
        boolean ignoreSpaces = row % 2 == 0;
        int logicalColumn = 0;
        for (int i = 0; i < rowString.length(); i++) {
            char character = rowString.charAt(i);
            if ((!ignoreSpaces || i % 2 != 1) && (ignoreSpaces || i % 2 != 0)) {
                Cell cell = from(character, row, logicalColumn);
                if (cell != null) {
                    result.add(cell);
                }

                logicalColumn++;
            }
        }

        return result;
    }

    private static Cell from(char character, int row, int column) {
        HexCoordinates coordinates =
                HexCoordinates.fromAxialCoordinates(column - (row / 2), row);
        BoardContent content = BOARD_CONTENT_BY_CHARACTER.get(character);
        return content != null
               ? new Cell(coordinates, content)
               : null;
    }
}