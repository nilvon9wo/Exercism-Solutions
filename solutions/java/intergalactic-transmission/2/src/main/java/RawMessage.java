import java.util.List;
import java.util.stream.Stream;

public class RawMessage {
    private final List<Integer> messageBytes;
    private final BitOperations bitOperations;
    public RawMessage(final List<Integer> messageBytes, BitOperations bitOperations) {
        this.messageBytes = List.copyOf(messageBytes);
        this.bitOperations = bitOperations;
    }

    public RawMessage(final List<Integer> messageBytes) {
        this(messageBytes, new BitOperations());
    }

    public Stream<Integer> getBits() {
        return this.messageBytes.stream()
            .flatMap(this.bitOperations::splitIntoBits);
    }
}
