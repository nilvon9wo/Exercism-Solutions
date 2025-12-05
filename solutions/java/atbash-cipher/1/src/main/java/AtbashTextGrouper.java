import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

record AtbashTextGrouper(int groupSize) {

    String groupText(String text) {
        int adjustedTextLength = text.length() + this.groupSize - 1;
        int groupCount = adjustedTextLength  / this.groupSize;
        List<String> groups = this.createGroups(text, groupCount);
        return String.join(" ", groups);
    }

    private List<String> createGroups(String text, int groupCount) {
        return IntStream.range(0, groupCount)
                       .mapToObj(index -> this.extractGroupSubstring(text, index))
                       .collect(Collectors.toList());
    }

    private String extractGroupSubstring(String text, int groupIndex) {
        int endIndex = this.calculateGroupEndIndex(text, groupIndex);
        return text.substring(groupIndex * this.groupSize, endIndex);
    }

    private int calculateGroupEndIndex(String text, int groupIndex) {
        int proposedEndIndex = (groupIndex + 1) * this.groupSize;
        return Math.min(proposedEndIndex, text.length());
    }
}
