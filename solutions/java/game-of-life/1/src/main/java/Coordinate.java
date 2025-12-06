public record Coordinate(int x, int y) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Coordinate(int x1, int y1))) {
            return false;
        }

        return this.x == x1
                       && this.y == y1;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}