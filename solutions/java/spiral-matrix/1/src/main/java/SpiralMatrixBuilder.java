class SpiralMatrixBuilder {

    int[][] buildMatrixOfSize(int size) {
        if (size == 0) {
            return new int[0][0];
        }

        SpiralMatrixState state = new SpiralMatrixState(size);
        while (state.getValue() <= state.getMaxValue()) {
            state.createTopRow()
                 .createRightColumn()
                 .createBottomRow()
                 .createLeftColumn();
        }

        return state.getMatrix();
    }
}