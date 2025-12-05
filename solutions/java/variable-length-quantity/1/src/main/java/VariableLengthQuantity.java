import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class VariableLengthQuantity {

    private static final int BITS_PER_CHUNK        = 7;
    private static final long DATA_MASK            = 0x7FL;
    private static final long CONTINUATION_BIT     = 0x80L;
    private static final String HEX_PREFIX         = "0x";
    private static final String INVALID_ENCODING_MESSAGE =
            "Invalid variable-length quantity encoding";

    List<String> encode(List<Long> numbers) {
        return numbers.stream()
                       .flatMap(this::encodeNumberToHexStream)
                       .collect(Collectors.toList());
    }

    private java.util.stream.Stream<String> encodeNumberToHexStream(long number) {
        int chunkCount = this.computeNumberOfChunks(number);
        return IntStream.range(0, chunkCount)
                       .mapToObj(index -> this.encodeChunkToHex(index, number, chunkCount));
    }

    private String encodeChunkToHex(int index, long number, int chunkCount) {
        int reverseIndex = chunkCount - 1 - index;
        int bitShift = BITS_PER_CHUNK * reverseIndex;
        long chunkValue = (number >> bitShift) & DATA_MASK;
        long byteValue = (index < chunkCount - 1)
                        ? (chunkValue | CONTINUATION_BIT)
                        : chunkValue;
        return this.toHexString(byteValue);
    }

    private int computeNumberOfChunks(long numberArg) {
        if (numberArg == 0) {
            return 1;
        }

        int usedBits = Long.SIZE - Long.numberOfLeadingZeros(numberArg);
        int roundedBits = usedBits + BITS_PER_CHUNK - 1;
        return Math.max(1, roundedBits / BITS_PER_CHUNK);
    }

    List<String> decode(List<Long> bytes) {
        List<Integer> endIndices = this.findEndIndices(bytes);

        if (this.isInvalidEncoding(bytes, endIndices)) {
            throw new IllegalArgumentException(INVALID_ENCODING_MESSAGE);
        }

        return this.buildDecodedHexList(bytes, endIndices);
    }

    private boolean isInvalidEncoding(List<Long> bytes, List<Integer> endIndices) {
        return endIndices.isEmpty()
                       || this.lastChunkDoesNotEndAtFinalByte(bytes, endIndices);
    }

    private boolean lastChunkDoesNotEndAtFinalByte(List<Long> bytes, List<Integer> endIndices) {
        return endIndices.getLast() != bytes.size() - 1;
    }

    private List<Integer> findEndIndices(List<Long> bytes) {
        return IntStream.range(0, bytes.size())
                       .filter(index -> (bytes.get(index) & CONTINUATION_BIT) == 0)
                       .boxed()
                       .collect(Collectors.toList());
    }

    private List<String> buildDecodedHexList(List<Long> bytes, List<Integer> endIndices) {
        return IntStream.range(0, endIndices.size())
                       .mapToObj(index -> this.sliceBytesForChunk(index, bytes, endIndices))
                       .map(this::decodeChunkToLong)
                       .map(this::toHexString)
                       .collect(Collectors.toList());
    }

    private List<Long> sliceBytesForChunk(int chunkIndex, List<Long> bytes, List<Integer> endIndices) {
        int end = endIndices.get(chunkIndex);
        int start = (chunkIndex == 0)
                        ? 0
                        : endIndices.get(chunkIndex - 1) + 1;
        return bytes.subList(start, end + 1);
    }

    private String toHexString(Long value) {
        return HEX_PREFIX + Long.toHexString(value);
    }

    private long decodeChunkToLong(List<Long> chunk) {
        return chunk.stream()
                       .reduce(0L, this.accumulationOperator());
    }

    private BinaryOperator<Long> accumulationOperator() {
        return (accumulator, byteValue) ->
                       (accumulator << BITS_PER_CHUNK) | (byteValue & DATA_MASK);
    }
}
