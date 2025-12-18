import java.util.List;
import java.util.stream.Stream;
public class EncodedSequence {
    private final List<Integer> encodedSequence;
    private final BitOperations bitOperations;
    public EncodedSequence(final List<Integer> encodedSequence, BitOperations bitOperations) {
        this.encodedSequence = List.copyOf(encodedSequence);
        this.bitOperations = bitOperations;
    }

    public EncodedSequence(final List<Integer> encodedSequence) {
        this(encodedSequence, new BitOperations());
    }

    public Stream<List<Integer>> getBits() {
        return this.encodedSequence.stream()
                                .map(this.bitOperations::byteToBitVector);
    }
}
