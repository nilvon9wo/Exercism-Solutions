import java.util.EnumSet;
import java.util.List;

record GrepContext(
        String pattern,
        EnumSet<GrepFlag> flags,
        String file,
        boolean multipleFiles
) {
    String getPatternToCompare() {
        return this.shouldIgnoreCase()
                       ? this.pattern.toLowerCase()
                       : this.pattern;
    }

    boolean hasFlag(GrepFlag flag) {
        return this.flags.contains(flag);
    }

    boolean shouldIgnoreCase() {
        return this.hasFlag(GrepFlag.IGNORE_CASE);
    }

    boolean shouldMatchEntireLine() {
        return this.hasFlag(GrepFlag.MATCH_ENTIRE_LINE);
    }

    boolean shouldInvert() {
        return this.hasFlag(GrepFlag.INVERT);
    }

    boolean shouldIncludeLineNumbers() {
        return this.hasFlag(GrepFlag.INCLUDE_LINE_NUMBERS);
    }

    boolean shouldIncludeFileNames() {
        return this.hasFlag(GrepFlag.INCLUDE_FILE_NAMES);
    }

    boolean hasMultipleFiles() {
        return this.multipleFiles;
    }
}
