import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

class BinarySearch {

    private final List<Integer> items;

    BinarySearch(List<Integer> items) {
        this.items = items;
    }

    int indexOf(int target) throws ValueNotFoundException {
        return searchRange(target, 0, items.size() - 1)
                .orElseThrow(() -> new ValueNotFoundException("Value not in array"));
    }

    private OptionalInt searchRange(int target, int left, int right) {
        if (left > right) {
            return OptionalInt.empty();
        }

        int mid = left + (right - left) / 2;
        int midValue = items.get(mid);
        if (midValue == target) {
            return OptionalInt.of(mid);
        }

        return midValue < target
               ? this.searchRange(target, mid + 1, right)
               : this.searchRange(target, left, mid - 1);
    }
}
