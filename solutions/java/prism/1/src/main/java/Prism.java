import java.util.*;
import java.util.stream.Stream;

public class Prism {
    public record LaserInfo(double x, double y, double angle){}
    public record PrismInfo(int id, double x, double y, double angle) {}

    public static List<Integer> findSequence(LaserInfo laser, List<PrismInfo> prisms) {
        return Stream.iterate(
                        new State(laser),
                        Objects::nonNull,
                        state -> state.nextState(prisms)
                )
                .skip(1)
                .map(State::hitId)
                .toList();
    }
}