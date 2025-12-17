import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class TransmissionDecoder {
    private final BitOperations  bitOperations;
    private final BitBlockGrouper bitBlockGrouper;
    private TransmissionDecoder(BitOperations  bitOperations, BitBlockGrouper bitBlockGrouper) {
        this.bitOperations = bitOperations;
        this.bitBlockGrouper = bitBlockGrouper;
    }

    public TransmissionDecoder() {
        this(new BitOperations(), new BitBlockGrouper());
    }

    private static final int DECODE_BLOCK_SIZE = 8;

    public List<Integer> getDecodedSequence(EncodedSequence encodedSequence) {
        List<Optional<List<Integer>>> optionalBitGroups = this.parseBlocks(encodedSequence);
        final boolean hasMissingBlocks = optionalBitGroups.stream()
                                                          .anyMatch(Optional::isEmpty);
        if (hasMissingBlocks) {
            throw new IllegalArgumentException();
        }

        return this.flattenDecodedBlocks(optionalBitGroups);
    }

    private List<Optional<List<Integer>>> parseBlocks(final EncodedSequence encodedSequence) {
        return encodedSequence.getBits()
                              .map(this::validateBlockParity)
                              .toList();
    }

    private Optional<List<Integer>> validateBlockParity(final List<Integer> eightBitBlock) {
        final Integer parityCheck = eightBitBlock.stream()
                                                 .reduce(0, this.bitOperations.getXorOperator());
        final boolean hasParityError = parityCheck != 0;
        return hasParityError
               ? Optional.empty()
               : Optional.of(eightBitBlock.subList(0, DECODE_BLOCK_SIZE - 1));
    }

    private List<Integer> flattenDecodedBlocks(final List<Optional<List<Integer>>> optionalBitGroups) {
        AtomicInteger blockCounter = new AtomicInteger(0);
        return optionalBitGroups
                .stream()
                .flatMap(this.convertOptionalBlockToBitStream())
                .collect(this.bitBlockGrouper.groupBitsByCounter(blockCounter, DECODE_BLOCK_SIZE))
                .values()
                .stream()
                .filter(block -> block.size() == DECODE_BLOCK_SIZE)
                .map(this.bitOperations::combineBlockBits)
                .toList();
    }

    private Function<Optional<List<Integer>>, Stream<? extends Integer>> convertOptionalBlockToBitStream() {
        return optionalBlock -> optionalBlock.stream()
                                             .flatMap(Collection::stream);
    }
}
