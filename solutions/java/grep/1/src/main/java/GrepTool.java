import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class GrepTool {
    private static final String LINE_SEPARATOR = "\n";
    private static final String COLON = ":";

    String grep(String pattern, List<String> flagStrings, List<String> files) {
        EnumSet<GrepFlag> flags = GrepFlag.fromStrings(flagStrings);
        boolean hasMultipleFiles = files.size() > 1;
        return files.stream()
                       .map(file -> new GrepContext(pattern, flags, file, hasMultipleFiles))
                       .map(this::processFile)
                       .filter(s -> !s.isEmpty())
                       .collect(Collectors.joining(LINE_SEPARATOR));
    }

    private String processFile(GrepContext context) {
        String file = context.file();
        List<String> lines = readFile(file);
        List<String> matches = IntStream.range(0, lines.size())
                                       .mapToObj(i -> processLine(context, lines.get(i), i))
                                       .filter(Objects::nonNull)
                                       .collect(Collectors.toList());

        return context.shouldIncludeFileNames() && !matches.isEmpty()
                       ? file
                       : String.join(LINE_SEPARATOR, matches);
    }

    private String processLine(GrepContext context, String line, int index) {
        String lineToCompare = context.shouldIgnoreCase() ? line.toLowerCase() : line;
        String patternToCompare = context.getPatternToCompare();

        boolean hasMatches = context.shouldMatchEntireLine()
                                  ? lineToCompare.equals(patternToCompare)
                                  : lineToCompare.contains(patternToCompare);

        if (context.shouldInvert()) {
            hasMatches = !hasMatches;
        }

        return hasMatches
                       ? this.formatMatch(context, line, index)
                       : null;
    }

    private String formatMatch(GrepContext context, String line, int index) {
        String result = line;

        if (context.shouldIncludeLineNumbers()) {
            result = (index + 1) + COLON + result;
        }

        if (context.hasMultipleFiles() && !context.shouldIncludeFileNames()) {
            result = context.file() + COLON + result;
        }

        return result;
    }

    private List<String> readFile(String file) {
        try {
            return Files.readAllLines(Paths.get(file));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
