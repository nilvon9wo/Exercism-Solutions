@FunctionalInterface
interface CellRenderer {
    void render(
            Position cellPosition,
            Position gridPosition,
            MazeCell cell
    );
}
