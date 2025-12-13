import java.util.List;
import java.util.stream.IntStream;

class BinarySearch {

    private final List<Integer> items;

    BinarySearch(List<Integer> items) {
        this.items = items;
    }

    int indexOf(int target) throws ValueNotFoundException {
        return IntStream.range(0, items.size())
                        .filter(i -> items.get(i) == target)
                        .findFirst()
                        .orElseThrow(() -> new ValueNotFoundException("Value not in array"));
    }
}
