public record HexCoordinates(int q, int r, int s) {

    public HexCoordinates {
        if (q + r + s != 0) {
            throw new IllegalArgumentException(
                    "Invalid axial coordinates: q + r + s must equal 0."
            );
        }
    }

    public static HexCoordinates fromAxialCoordinates(int q, int r) {
        int s = -q - r;
        return new HexCoordinates(q, r, s);
    }
}