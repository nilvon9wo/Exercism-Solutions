public class SpiralMatrixState {
    public SpiralMatrixState(int size) {
        this.matrix = new int[size][size];
        this.bottomRow = size - 1;
        this.rightColumn = size - 1;
        this.maxValue = size * size;
    }

    private final int[][] matrix;
    public int[][] getMatrix() {
        return matrix;
    }

    private int topRow = 0;
    private int bottomRow;
    private int leftColumn = 0;
    private int rightColumn;

    private int value = 1;
    public int getValue() {
        return this.value;
    }

    private final int maxValue;
    public int getMaxValue() {
        return this.maxValue;
    }

    public SpiralMatrixState createTopRow() {
        TraversalSegment traversalSegment
                = new TraversalSegment(TraversalAxis.ROW, topRow, leftColumn, rightColumn, 1);
        return this.fillAndShrink(traversalSegment, () -> topRow++);
    }

    public SpiralMatrixState createRightColumn() {
        TraversalSegment traversalSegment
                = new TraversalSegment(TraversalAxis.COLUMN, rightColumn, topRow, bottomRow, 1);
        return this.fillAndShrink(traversalSegment, () -> rightColumn--);
    }

    public SpiralMatrixState createBottomRow() {
        TraversalSegment traversalSegment
                = new TraversalSegment(TraversalAxis.ROW, bottomRow, rightColumn, leftColumn, -1);
        return this.fillAndShrink(traversalSegment, () -> bottomRow--);
    }

    @SuppressWarnings("UnusedReturnValue")
    public SpiralMatrixState createLeftColumn() {
        TraversalSegment traversalSegment
                = new TraversalSegment(TraversalAxis.COLUMN, leftColumn, bottomRow, topRow, -1);
        return this.fillAndShrink(traversalSegment, () -> leftColumn++);
    }

    private SpiralMatrixState fillAndShrink(
            TraversalSegment traversalSegment,
            Runnable shrinkBoundary
    ) {
        this.fillLine(traversalSegment);
        shrinkBoundary.run();
        return this;
    }

    private void fillLine(TraversalSegment traversalSegment) {
        for (
                int i = traversalSegment.from();
                this.isWithinBounds(traversalSegment, i);
                i += traversalSegment.step()
        ) {
            if (traversalSegment.axis() == TraversalAxis.ROW) {
                this.setAndAdvance(traversalSegment.fixedIndex(), i);
            }
            else {
                this.setAndAdvance(i, traversalSegment.fixedIndex());
            }
        }
    }

    private boolean isWithinBounds(final TraversalSegment traversalSegment, final int i) {
        return traversalSegment.step() > 0
               ? i <= traversalSegment.to()
               : i >= traversalSegment.to();
    }

    public void setAndAdvance(final int top, final int column) {
        this.matrix[top][column] = this.value++;
    }
}
