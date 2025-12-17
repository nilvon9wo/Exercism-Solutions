import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BitBlockGrouper {
    public Collector<Integer, ?, Map<Integer, List<Integer>>> groupBitsByCounter(
            final AtomicInteger bitCounter,
            final int blockSize
    ) {
        return Collectors.groupingBy(bit -> this.getBlockIndex(bitCounter, blockSize));
    }

    private int getBlockIndex(final AtomicInteger bitCounter, final int blockSize) {
        final int currentIndex = bitCounter.getAndIncrement();
        return currentIndex / blockSize;
    }
}
