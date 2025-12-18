import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.*;

public class IntergalacticTransmission {
    private static final int BIT_COUNT_PER_BYTE = 8;
    private static final int ENCODE_BLOCK_SIZE = 7;
    private static final int DECODE_BLOCK_SIZE = 8;

    private static IntergalacticTransmission singletonTransmission;

    private static IntergalacticTransmission getSingletonTransmission() {
        if (singletonTransmission == null) {
            singletonTransmission = new IntergalacticTransmission();
        }

        return singletonTransmission;
    }

    public static List<Integer> getTransmitSequence(List<Integer> messageBits) {
        return getSingletonTransmission()
                .getEncodedSequence(messageBits);
    }

    public List<Integer> getEncodedSequence(List<Integer> messageBits) {
        AtomicInteger bitGroupCounter = new AtomicInteger(0);
        return messageBits.stream()
                   .flatMap(this::splitIntoBits)
                   .collect(this.groupBitsByCounter(bitGroupCounter, ENCODE_BLOCK_SIZE))
                   .values()
                   .stream()
                   .map(this::calculateEncodedBlock)
                   .toList();
    }

    private Stream<Integer> splitIntoBits(Integer number) {
        return IntStream.range(0, BIT_COUNT_PER_BYTE)
                        .map(i -> this.getBitValue(number, i))
                        .boxed();
    }

    private int extractBitAtIndex(final Integer number, final int i) {
        return number >> (BIT_COUNT_PER_BYTE - 1 - i);
    }

    private int calculateEncodedBlock(final List<Integer> sevenBitBlock) {
        int shiftedValue = this.shiftEncodedBlock(sevenBitBlock);
        int parityBit = sevenBitBlock.stream()
                                     .reduce(0, this.getXorOperator());
        return this.combineBits(shiftedValue, parityBit);
    }

    private int shiftEncodedBlock(final List<Integer> sevenBitBlock) {
        int encodedValue = sevenBitBlock.stream()
                                        .reduce(0, this::combineBits);
        int bitsToShift = ENCODE_BLOCK_SIZE - sevenBitBlock.size();
                return encodedValue << bitsToShift;
    }

    public static List<Integer> decodeSequence(List<Integer> encodedSequence) {
        return getSingletonTransmission()
                .getDecodedSequence(encodedSequence);
    }

    public List<Integer> getDecodedSequence(List<Integer> encodedSequence) {
        List<Optional<List<Integer>>> optionalBitGroups = this.parseBlocks(encodedSequence);
        final boolean hasMissingBlocks = optionalBitGroups.stream()
                                             .anyMatch(Optional::isEmpty);
        if (hasMissingBlocks) {
            throw new IllegalArgumentException();
        }

        return this.flattenDecodedBlocks(optionalBitGroups);
    }

    private List<Optional<List<Integer>>> parseBlocks(final List<Integer> encodedSequence) {
        return encodedSequence.stream()
                   .map(this::convertIntegerToBits)
                   .map(this::validateBlockParity)
                   .toList();
    }

    private List<Integer> convertIntegerToBits(Integer number) {
        return IntStream.range(0, BIT_COUNT_PER_BYTE)
                        .map(i -> this.getBitValue(number, i))
                        .boxed()
                        .toList();
    }

    private int getBitValue(final Integer number, final int i) {
        return this.extractBitAtIndex(number, i) & 1;
    }

    private Optional<List<Integer>> validateBlockParity(final List<Integer> eightBitBlock) {
        final Integer parityCheck = eightBitBlock.stream()
                                        .reduce(0, this.getXorOperator());
        final boolean hasParityError = parityCheck != 0;
        return hasParityError
               ? Optional.empty()
               : Optional.of(eightBitBlock.subList(0, DECODE_BLOCK_SIZE - 1));

    }

    private BinaryOperator<Integer> getXorOperator() {
        return (left, right) -> left ^ right;
    }

    private List<Integer> flattenDecodedBlocks(final List<Optional<List<Integer>>> optionalBitGroups) {
        AtomicInteger blockCounter = new AtomicInteger(0);
        return optionalBitGroups
                .stream()
                .flatMap(this.convertOptionalBlockToBitStream())
                .collect(this.groupBitsByCounter(blockCounter, DECODE_BLOCK_SIZE))
                .values()
                .stream()
                .filter(block -> block.size() == DECODE_BLOCK_SIZE)
                .map(this::combineBlockBits)
                .toList();
    }

    private Function<Optional<List<Integer>>, Stream<? extends Integer>> convertOptionalBlockToBitStream() {
        return optionalBlock -> optionalBlock.stream()
                                             .flatMap(Collection::stream);
    }

    private Collector<Integer, ?, Map<Integer, List<Integer>>> groupBitsByCounter(
            final AtomicInteger bitCounter,
            final int blockSize
    ) {
        return Collectors.groupingBy(bit -> this.getBlockIndex(bitCounter, blockSize));
    }

    private int getBlockIndex(final AtomicInteger bitCounter, final int blockSize) {
        final int currentIndex = bitCounter.getAndIncrement();
        return currentIndex / blockSize;
    }

    private Integer combineBlockBits(final List<Integer> bitBlock) {
        return bitBlock.stream()
                   .reduce(0, this::combineBits);
    }

    private int combineBits(final Integer accumulatedBits, final Integer nextBit) {
        final int shifted = accumulatedBits << 1;
        return shifted | nextBit;
    }
}
