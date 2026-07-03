import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

class RailFenceCipher {
    private final int rows;

    RailFenceCipher(int rows) {
        this.rows = rows;
    }

    String getEncryptedData(String message) {
        if (rows == 1 || message.length() <= 1) {
            return message;
        }

        StringBuilder[] rails = initRails(rows);
        int[] railPattern = buildRailPattern(message.length());
        IntStream.range(0, message.length())
                 .forEach(i ->this.appendToRail(rails[railPattern[i]], message.charAt(i)));

        StringBuilder result = new StringBuilder(message.length());
        Arrays.stream(rails)
              .forEach(result::append);
        return result.toString();
    }

    @SuppressWarnings("UnusedReturnValue")
    private StringBuilder appendToRail(final StringBuilder rails, final char message) {
        return rails.append(message);
    }

    String getDecryptedData(String message) {
        if (rows == 1 || message.length() <= 1) {
            return message;
        }

        int[] pattern = this.buildRailPattern(message.length());
        int[] counts = this.countCharacters(pattern);
        char[][] rails = this.sliceCipherTextIntoRails(message, counts);
        return this.reconstructUsingPattern(message, pattern, rails)
                   .toString();
    }

    private StringBuilder reconstructUsingPattern(
            final String message,
            final int[] railPattern,
            final char[][] rails
    ) {
        int[] position = new int[rows];
        StringBuilder result = new StringBuilder(message.length());
        IntStream.of(railPattern)
                 .forEach(this.consumeRailCharacter(rails, position, result));
        return result;
    }

    private IntConsumer consumeRailCharacter(
            final char[][] rails,
            final int[] position,
            final StringBuilder result
    ) {
        return rail ->
                result.append(rails[rail][position[rail]++]);
    }

    private char[][] sliceCipherTextIntoRails(final String message, final int[] counts) {
        char[] chars = message.toCharArray();
        int[] offsets = this.buildOffsets(counts);
        return IntStream.range(0, rows)
                        .mapToObj(this.createRailSlice(counts, chars, offsets))
                        .toArray(char[][]::new);
    }

    private IntFunction<char[]> createRailSlice(
            final int[] counts,
            final char[] characters,
            final int[] offsets
    ) {
        return row -> {
            char[] rail = new char[counts[row]];
            System.arraycopy(characters, offsets[row], rail, 0, counts[row]);
            return rail;
        };
    }

    private int[] buildOffsets(int[] counts) {
        int[] offsets = new int[counts.length];

        int offset = 0;
        for (int i = 0; i < counts.length; i++) {
            offsets[i] = offset;
            offset += counts[i];
        }

        return offsets;
    }

    private int[] countCharacters(final int[] pattern) {
        int[] counts = new int[rows];
        for (int rail : pattern) {
            counts[rail]++;
        }
        return counts;
    }

    private int[] buildRailPattern(int length) {
        int[] rail = {0};
        int[] direction = {1};
        return IntStream.range(0, length)
                        .map(i -> this.advanceZigZagRail(rail, direction))
                        .toArray();
    }

    private int advanceZigZagRail(final int[] rail, final int[] direction) {
        int current = rail[0];

        if (rail[0] == 0) {
            direction[0] = 1;
        } else if (rail[0] == rows - 1) {
            direction[0] = -1;
        }

        rail[0] += direction[0];
        return current;
    }

    private StringBuilder[] initRails(int rows) {
        StringBuilder[] rails = new StringBuilder[rows];
        for (int i = 0; i < rows; i++) {
            rails[i] = new StringBuilder();
        }
        return rails;
    }
}