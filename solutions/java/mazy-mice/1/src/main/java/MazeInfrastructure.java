import java.util.Arrays;

public class MazeInfrastructure {
    private MazeInfrastructure(Infrastructure[][] grid) {
        this.grid = grid;
        this.height = grid.length;
        this.width = grid[0].length;
    }

    private final Infrastructure[][] grid;
    public Infrastructure get(Position position) {
        return this.grid[position.row()][position.column()];
    }
    @SuppressWarnings("UnusedReturnValue")
    public MazeInfrastructure set(Position position, Infrastructure infrastructure) {
        this.grid[position.row()][position.column()] = infrastructure;
        return this;
    }

    private final int height;
    public int  getHeight() {
        return height;
    }
    private final int width;
    public int getWidth() {
        return width;
    }

    public static MazeInfrastructure create(MazeContext context) {
        int height = context.getNumberOfRows() * 2 + 1;
        int width = context.getNumberOfColumns() * 2 + 1;
        Infrastructure[][] grid = new Infrastructure[height][width];

        for (int row = 0; row < height; row++) {
            Arrays.fill(grid[row], Infrastructure.SPACE);
        }

        return new  MazeInfrastructure(grid);
    }
}
