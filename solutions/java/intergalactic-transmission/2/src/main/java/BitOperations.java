import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BitOperations {
    public static final int BIT_COUNT_PER_BYTE = 8;

    public Stream<Integer> splitIntoBits(Integer number) {
        return IntStream.range(0, BIT_COUNT_PER_BYTE)
                        .map(i -> this.getBitValue(number, i))
                        .boxed();
    }

    public List<Integer> byteToBitVector(Integer number) {
        return IntStream.range(0, BIT_COUNT_PER_BYTE)
                        .map(i -> this.getBitValue(number, i))
                        .boxed()
                        .toList();
    }

    private int getBitValue(final Integer number, final int i) {
        return this.extractBitAtIndex(number, i) & 1;
    }

    private int extractBitAtIndex(final Integer number, final int i) {
        return number >> (BIT_COUNT_PER_BYTE - 1 - i);
    }

    public BinaryOperator<Integer> getXorOperator() {
        return (left, right) -> left ^ right;
    }

    public int combineBits(final Integer accumulatedBits, final Integer nextBit) {
        final int shifted = accumulatedBits << 1;
        return shifted | nextBit;
    }

    public Integer combineBlockBits(final List<Integer> bitBlock) {
        return bitBlock.stream()
                       .reduce(0, this::combineBits);
    }
}
