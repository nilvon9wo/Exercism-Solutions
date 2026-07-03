import java.util.OptionalDouble;
import java.util.OptionalInt;

public class PuzzleConfiguration {

    private static final double FLOAT_EQUALITY_TOLERANCE = 1e-9;
    private static final int BORDER_EDGE_WIDTH = 2;
    private static final double SQUARE_ASPECT_RATIO = 1.0;

    private final JigsawInfo puzzleInfo;

    public PuzzleConfiguration(JigsawInfo puzzleInfo) {
        this.puzzleInfo = puzzleInfo;
    }

    public int matchedRow = -1;
    public int matchedColumn = -1;
    public int matchCount = 0;

    public boolean violatesCoreConstraints(Position position) {
        return isPresentAndUnequal(puzzleInfo.getRows(), position.row())
               || isPresentAndUnequal(puzzleInfo.getColumns(), position.column())
               || violatesAspectRatioConstraint(position);
    }

    private boolean violatesAspectRatioConstraint(Position position) {
        int row = position.row();
        int column = position.column();
        double aspectRatio = (double) column / row;

        Double aspectRatioHint = resolveAspectRatioHint();
        return aspectRatioHint != null
               && Math.abs(aspectRatio - aspectRatioHint) > FLOAT_EQUALITY_TOLERANCE;
    }

    private Double resolveAspectRatioHint() {
        final JigsawInfo puzzleInfo = this.puzzleInfo;
        OptionalDouble inputAspectRatio = puzzleInfo.getAspectRatio();
        String format = puzzleInfo.getFormat().orElse(null);

        if ("square".equals(format)) {
            return SQUARE_ASPECT_RATIO;
        }

        return inputAspectRatio.isPresent()
               ? inputAspectRatio.getAsDouble()
               : null;
    }

    public boolean violatesDerivedConstraints(Position position) {
        int row = position.row();
        int column = position.column();

        int totalPieces = row * column;
        int insidePieces = (row - BORDER_EDGE_WIDTH) * (column - BORDER_EDGE_WIDTH);
        int borderPieces = totalPieces - insidePieces;

        final JigsawInfo puzzleInfo = this.puzzleInfo;

        return this.isPresentAndUnequal(puzzleInfo.getPieces(), totalPieces)
               || this.isPresentAndUnequal(puzzleInfo.getInside(), insidePieces)
               || this.isPresentAndUnequal(puzzleInfo.getBorder(), borderPieces);
    }

    private boolean isPresentAndUnequal(OptionalInt expectedValue, int actualValue) {
        return expectedValue.isPresent()
               && actualValue != expectedValue.getAsInt();
    }

    public boolean violatesFormatConstraint(Position position) {
        String format = puzzleInfo.getFormat().orElse(null);

        if (format == null) {
            return false;
        }

        int row = position.row();
        int column = position.column();

        return switch (format) {
            case "square" -> row != column;
            case "portrait" -> column >= row;
            case "landscape" -> column <= row;
            default -> false;
        };
    }

    public JigsawInfo toJigsawInfo() {
        int totalPieces = matchedRow * matchedColumn;
        int insidePieces = (matchedRow - BORDER_EDGE_WIDTH) * (matchedColumn - BORDER_EDGE_WIDTH);
        double aspectRatio = (double) matchedColumn / matchedRow;

        return new JigsawInfo.Builder()
                .rows(matchedRow)
                .columns(matchedColumn)
                .pieces(totalPieces)
                .inside(insidePieces)
                .border(totalPieces - insidePieces)
                .aspectRatio(aspectRatio)
                .format(determineFormat(aspectRatio))
                .build();
    }

    private static String determineFormat(final double aspectRatio) {
        if (Math.abs(aspectRatio - SQUARE_ASPECT_RATIO) < FLOAT_EQUALITY_TOLERANCE) {
            return "square";
        } else if (aspectRatio < SQUARE_ASPECT_RATIO) {
            return "portrait";
        } else {
            return "landscape";
        }
    }
}