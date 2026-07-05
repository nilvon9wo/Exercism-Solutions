import java.util.*;

public class MazeGenerator {
    private final MazeGridRenderer renderer;
    private MazeGenerator(MazeGridRenderer renderer) {
        this.renderer = renderer;
    }

    public MazeGenerator() {
        this(new  MazeGridRenderer());
    }

    public char[][] generatePerfectMaze(int rows, int columns) {
        return generateMaze(new MazeContext(rows, columns, new Random()));
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        return generateMaze(new MazeContext(rows, columns, new Random(seed)));
    }

    private char[][] generateMaze(MazeContext context) {
        context.setGrid(MazeGrid.create(context));
        carveMazeFrom(context, new Position(0, 0));
        return this.renderer.renderMaze(context);
    }

    private void carveMazeFrom(MazeContext context, Position startPosition) {
        MazeGrid grid = context.getGrid();
        MazeCell startCell = grid.get(startPosition);
        startCell.visited = true;
        List<int[]> shuffledDirections = this.createShuffledDirections(context);

        for (int[] direction : shuffledDirections) {
            Position nextPosition = startPosition.nextPosition(direction[0], direction[1]);
            if (this.isInsideMaze(context, nextPosition) && !grid.get(nextPosition).visited) {
                this.removeWallBetween(grid, startPosition, nextPosition);
                this.carveMazeFrom(context, nextPosition);
            }
        }
    }

    private List<int[]> createShuffledDirections(MazeContext context) {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-1, 0});
        directions.add(new int[]{1, 0});
        directions.add(new int[]{0, -1});
        directions.add(new int[]{0, 1});

        Collections.shuffle(directions, context.getRandom());
        return directions;
    }

    private boolean isInsideMaze(MazeContext context, Position position) {
        int row = position.row();
        int column = position.column();
        return row >= 0 && row < context.getNumberOfRows()
               && column >= 0 && column < context.getNumberOfColumns();
    }

    private void removeWallBetween(MazeGrid grid, Position fromPosition, Position toPosition) {
        MazeCell fromCell = grid.get(fromPosition);
        MazeCell toCell = grid.get(toPosition);

        int fromRow = fromPosition.row();
        int toRow = toPosition.row();
        int fromColumn = fromPosition.column();
        int toColumn = toPosition.column();

        if (toRow == fromRow - 1) {
            fromCell.hasNorthWall = false;
            toCell.hasSouthWall = false;
        }
        else if (toRow == fromRow + 1) {
            fromCell.hasSouthWall = false;
            toCell.hasNorthWall = false;
        }
        else if (toColumn == fromColumn - 1) {
            fromCell.hasWestWall = false;
            toCell.hasEastWall = false;
        }
        else if (toColumn == fromColumn + 1) {
            fromCell.hasEastWall = false;
            toCell.hasWestWall = false;
        }
    }
}