public record Vector(double x, double y) {
    public double getDistanceAlongRay(Prism.PrismInfo prism, Direction direction) {
        return toPrismVector(prism)
                .projectOntoRay(direction);
    }
    private Vector toPrismVector(Prism.PrismInfo prism) {
        return new Vector(
                prism.x() - x,
                prism.y() - y
        );
    }

    private double projectOntoRay(Direction direction) {
        return x * direction.x()
                + y * direction.y();
    }

    public double getErrorSquared(
            Prism.PrismInfo prism,
            Direction direction,
            double distanceAlongRay
    ) {
        return toProjectPoint(direction, distanceAlongRay)
                .computePerpendicularErrorSquared(prism);
    }

    private Vector toProjectPoint(Direction direction, double distance) {
        return new Vector(
                x + distance * direction.x(),
                y + distance * direction.y()
        );
    }

    private double computePerpendicularErrorSquared(Prism.PrismInfo prism) {
        double dx = x - prism.x();
        double dy = y - prism.y();
        return dx * dx + dy * dy;
    }

    public static Vector from(State state) {
        return new Vector(state.x(), state.y());
    }
}

