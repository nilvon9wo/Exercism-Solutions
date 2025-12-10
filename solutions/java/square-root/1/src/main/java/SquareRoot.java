import java.util.stream.IntStream;

public class SquareRoot {

    public int squareRoot(int radicand) {
        if (radicand < 0) {
            throw new IllegalArgumentException("radicand must be non-negative");
        }

        int maxCandidate = (int) Math.ceil(Math.sqrt(radicand));
        return IntStream.rangeClosed(1, maxCandidate)
                       .filter(i -> i * i == radicand)
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("No integer square root found"));
    }
}
