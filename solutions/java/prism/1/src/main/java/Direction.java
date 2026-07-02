public record Direction(double x, double y) {

    public static Direction from(double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        return new Direction(
                Math.cos(radians),
                Math.sin(radians)
        );
    }
}
