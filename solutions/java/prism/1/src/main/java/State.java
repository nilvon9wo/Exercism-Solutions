import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record State(double x, double y, double angle, Integer hitId) {
    State(double x, double y, double angle) {
        this(x, y, angle, null);
    }
    State(Prism.LaserInfo laserInfo) {
        this(laserInfo.x(),  laserInfo.y(), laserInfo.angle());
    }

    public State nextState(List<Prism.PrismInfo> prisms) {
        Hit hit = this.findNextHit(prisms);
        if (hit == null) {
            return null;
        }

        return new State(
                hit.prism.x(),
                hit.prism.y(),
                this.normalize(angle + hit.prism.angle()),
                hit.prism.id()
        );
    }

    private double normalize(double angle) {
        angle %= 360.0;
        if (angle <= -180) {
            angle += 360;
        }

        if (angle > 180) {
            angle -= 360;
        }

        return angle;
    }

    private Hit findNextHit(List<Prism.PrismInfo> prisms) {
        return prisms.stream()
                .map(prismInfo -> HitCandidate.from(this, prismInfo))
                .filter(Objects::nonNull)
                .filter(HitCandidate::isValidHit)
                .min(Comparator.comparingDouble(HitCandidate::distanceAlongRay))
                .map(candidate -> new Hit(candidate.targetPrism()))
                .orElse(null);
    }
}