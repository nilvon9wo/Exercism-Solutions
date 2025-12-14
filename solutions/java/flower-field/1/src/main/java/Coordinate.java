public record Coordinate(int x, int y) {
    public Coordinate offsetBy(final int dx, final Integer dy) {
        return new Coordinate(this.x() + dx, this.y() + dy);
    }
}