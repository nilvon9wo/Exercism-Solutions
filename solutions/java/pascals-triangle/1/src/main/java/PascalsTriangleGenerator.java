import java.util.stream.IntStream;

class PascalsTriangleGenerator {

    int[][] generateTriangle(int rowCount) {
        return rowCount == 0
                       ? new int[0][]
                       : this.buildRows(rowCount);
    }

    private int[][] buildRows(int rowCount) {
        return IntStream.range(0, rowCount)
                       .mapToObj(this::buildRow)
                       .toArray(int[][]::new);
    }

    private int[] buildRow(int rowIndex) {
        return rowIndex == 0
                       ? new int[]{1}
                       : this.nextRow(this.generateTriangle(rowIndex));
    }

    private int[] nextRow(int[][] triangleSoFar) {
        int[] previousRow = triangleSoFar[triangleSoFar.length - 1];
        int size = previousRow.length + 1;
        return IntStream.range(0, size)
                       .map(index -> this.valueAtPosition(index, size, previousRow))
                       .toArray();
    }

    private int valueAtPosition(int index, int size, int[] previousRow) {
        return (index == 0 || index == size - 1)
                       ? 1
                       : previousRow[index - 1] + previousRow[index];
    }
}
