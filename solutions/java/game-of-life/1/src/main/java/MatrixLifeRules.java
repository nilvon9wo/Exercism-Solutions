public record MatrixLifeRules(MatrixNeighbors matrixNeighbors) {
    public MatrixLifeRules() {
        this(new MatrixNeighbors());
    }

    private static final int LIVE_CELL_SURVIVE_MIN_NEIGHBORS = 2;
    private static final int LIVE_CELL_SURVIVE_MAX_NEIGHBORS = 3;
    private static final int DEAD_CELL_COME_ALIVE_NEIGHBORS = 3;

    public boolean willHaveLifeInNextState(LifeGrid lifeGrid, Coordinate coordinate) {
        boolean currentlyHasLife = this.hasLifeByCoordinates(lifeGrid, coordinate);
        long liveNeighborCount = this.countLiveNeighbors(lifeGrid, coordinate);
        boolean liveCellSurvives = currentlyHasLife
                                           && this.doesLiveCellSurvive(liveNeighborCount);
        return liveCellSurvives
                       || this.doesDeadCellComeAlive(currentlyHasLife, liveNeighborCount);
    }

    private long countLiveNeighbors(LifeGrid lifeGrid, Coordinate coordinate) {
        return this.matrixNeighbors.getNeighborCoordinates(coordinate)
                       .stream()
                       .filter(foo ->  this.hasLifeByCoordinates(lifeGrid, foo))
                       .count();
    }

    public Boolean hasLifeByCoordinates(LifeGrid lifeGrid, Coordinate neighbor) {
        return lifeGrid.hasLife(neighbor);
    }

    private boolean doesLiveCellSurvive(long liveNeighborCount) {
        return liveNeighborCount >= LIVE_CELL_SURVIVE_MIN_NEIGHBORS
                       && liveNeighborCount <= LIVE_CELL_SURVIVE_MAX_NEIGHBORS;
    }

    private boolean doesDeadCellComeAlive(boolean currentlyHasLife, long liveNeighborCount) {
        return !currentlyHasLife
                       && liveNeighborCount == DEAD_CELL_COME_ALIVE_NEIGHBORS;
    }


}
