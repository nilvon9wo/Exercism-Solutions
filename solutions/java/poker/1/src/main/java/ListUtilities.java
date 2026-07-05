import java.util.List;

public class ListUtilities {
    public static <T extends Comparable<? super T>> int compareTo(
            List<T> list1,
            List<T> list2
    ) {
        int minLength = Math.min(list1.size(), list2.size());
        for (int i = 0; i < minLength; i++) {
            int comparison = list1.get(i).compareTo(list2.get(i));

            if (comparison != 0) {
                return comparison;
            }
        }

        return Integer.compare(list1.size(), list2.size());
    }
}
