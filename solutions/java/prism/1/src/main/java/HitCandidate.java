public record HitCandidate(
        Prism.PrismInfo targetPrism,
        double distanceAlongRay,
        double perpendicularErrorSquared
) {

    private static final double MIN_FORWARD_DISTANCE = 1e-6;
    private static final double HIT_TOLERANCE_SQUARED = 1e-4;

    public boolean isValidHit() {
        return perpendicularErrorSquared <= HIT_TOLERANCE_SQUARED;
    }

    public static HitCandidate from(State laserState, Prism.PrismInfo prism) {
        Direction direction = Direction.from(laserState.angle());
        Vector origin = Vector.from(laserState);
        double distanceAlongRay = origin.getDistanceAlongRay(prism, direction);
        if (!isInFrontOfLaser(distanceAlongRay)) {
            return null;
        }

        double errorSquared = origin.getErrorSquared(prism, direction, distanceAlongRay);
        return new HitCandidate(prism, distanceAlongRay, errorSquared);
    }

    private static boolean isInFrontOfLaser(double distanceAlongRay) {
        return distanceAlongRay > MIN_FORWARD_DISTANCE;
    }
}