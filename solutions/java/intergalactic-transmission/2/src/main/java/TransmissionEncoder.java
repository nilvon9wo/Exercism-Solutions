import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransmissionEncoder {
    private final BitOperations  bitOperations;
    private final BitBlockGrouper bitBlockGrouper;
    private TransmissionEncoder(BitOperations  bitOperations, BitBlockGrouper bitBlockGrouper) {
        this.bitOperations = bitOperations;
        this.bitBlockGrouper = bitBlockGrouper;
    }

    public TransmissionEncoder() {
        this(new BitOperations(), new BitBlockGrouper());
    }

    private static final int ENCODE_BLOCK_SIZE = 7;

    public List<Integer> getEncodedSequence(RawMessage message) {
        AtomicInteger bitGroupCounter = new AtomicInteger(0);
        return message.getBits()
                          .collect(this.bitBlockGrouper.groupBitsByCounter(bitGroupCounter, ENCODE_BLOCK_SIZE))
                          .values()
                          .stream()
                          .map(this::calculateEncodedBlock)
                          .toList();
    }

    private int calculateEncodedBlock(final List<Integer> sevenBitBlock) {
        int shiftedValue = this.shiftEncodedBlock(sevenBitBlock);
        int parityBit = sevenBitBlock.stream()
                                     .reduce(0, this.bitOperations.getXorOperator());
        return this.bitOperations.combineBits(shiftedValue, parityBit);
    }

    private int shiftEncodedBlock(final List<Integer> sevenBitBlock) {
        int encodedValue = sevenBitBlock.stream()
                                        .reduce(0, this.bitOperations::combineBits);
        int bitsToShift = ENCODE_BLOCK_SIZE - sevenBitBlock.size();
        return encodedValue << bitsToShift;
    }

}
