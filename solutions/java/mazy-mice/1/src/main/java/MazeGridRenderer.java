public class MazeGridRenderer {
    public char[][] renderMaze(MazeContext context) {
        MazeInfrastructure maze = MazeInfrastructure.create(context);
        this.renderCells(context, maze);
        this.renderIntersections(maze);
        this.renderCorners(maze);
        this.renderEntranceAndExit(maze);
        return toCharGrid(maze);
    }



    private void renderCells(MazeContext context, MazeInfrastructure maze) {
        forEachCell(
                (cellPosition, gridPosition, cell)
                        -> renderCellWalls(maze, gridPosition, cell),
                context
        );
    }

    private void renderCellWalls(MazeInfrastructure maze, Position gridPosition, MazeCell cell) {
        int gridRow = gridPosition.row();
        int gridColumn = gridPosition.column();
        if (cell.hasNorthWall) {
            maze.set(new Position(gridRow - 1, gridColumn), Infrastructure.HORIZONTAL_WALL);
        }
        if (cell.hasSouthWall) {
            maze.set(new Position(gridRow + 1, gridColumn), Infrastructure.HORIZONTAL_WALL);
        }
        if (cell.hasWestWall) {
            maze.set(new Position(gridRow, gridColumn - 1), Infrastructure.VERTICAL_WALL);
        }
        if (cell.hasEastWall) {
            maze.set(new Position(gridRow, gridColumn + 1), Infrastructure.VERTICAL_WALL);
        }
    }

    private void renderIntersections(MazeInfrastructure maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        for (int row = 0; row < height; row += 2) {
            for (int column = 0; column < width; column += 2) {
                maze.set(new Position(row, column), Infrastructure.INTERSECTION);
            }
        }
    }

    private void renderCorners(MazeInfrastructure maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();

        maze.set(new Position(0, 0), Infrastructure.TOP_LEFT_CORNER);
        maze.set(new Position(0, width - 1), Infrastructure.TOP_RIGHT_CORNER);
        maze.set(new Position(height - 1, 0), Infrastructure.BOTTOM_LEFT_CORNER);
        maze.set(new Position(height - 1, width - 1), Infrastructure.BOTTOM_RIGHT_CORNER);
    }

    private void renderEntranceAndExit(MazeInfrastructure maze) {
        maze.set(new Position(1, 0), Infrastructure.ENTRANCE_EXIT_SYMBOL);

        int height = maze.getHeight();
        int exitRow = height - 2;
        int width = maze.getWidth();
        int exitColumn = width - 1;
        maze.set(new Position(exitRow, exitColumn), Infrastructure.ENTRANCE_EXIT_SYMBOL);
    }

    private void forEachCell(CellRenderer renderer, MazeContext context) {
        for (int row = 0; row < context.getNumberOfRows(); row++) {
            for (int column = 0; column < context.getNumberOfColumns(); column++) {
                Position cellPosition = new Position(row, column);
                int gridRow = row * 2 + 1;
                int gridColumn = column * 2 + 1;
                Position gridPosition = new Position(gridRow, gridColumn);
                MazeCell cell = context.getGrid()
                                       .get(new Position(row, column));
                renderer.render(cellPosition, gridPosition, cell);
            }
        }
    }

    private static char[][] toCharGrid(MazeInfrastructure maze) {
        int rows = maze.getHeight();
        int columns = maze.getWidth();
        char[][] charGrid = new char[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                charGrid[row][column] = maze.get(new Position(row, column))
                                            .symbol();
            }
        }

        return charGrid;
    }
}
