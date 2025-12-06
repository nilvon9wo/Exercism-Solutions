public record GameOfLife(MatrixFactory matrixFactory) {
    public GameOfLife() {
        this(new MatrixFactory());
    }

    public int[][] tick(int[][] matrix) {
        return this.matrixFactory.from(matrix)
                       .toNextState()
                       .toIntArray();
    }
}



