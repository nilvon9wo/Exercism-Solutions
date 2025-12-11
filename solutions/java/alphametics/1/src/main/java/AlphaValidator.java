import java.util.stream.IntStream;

public class AlphaValidator {
    public boolean assignmentProducesCorrectSum(SolverContext context) {
        long sumOfAddends = this.computeSumOfAllAddends(context);
        String result = context.puzzle()
                                .result();
        long valueOfResult = this.computeWordValue(context, result);

        return sumOfAddends == valueOfResult;
    }

    private long computeSumOfAllAddends(SolverContext context) {
        Puzzle puzzle = context.puzzle();
        return puzzle.addends()
                       .stream()
                       .mapToLong(word -> this.computeWordValue(context, word))
                       .sum();
    }

    private long computeWordValue(SolverContext context, String word) {
        return IntStream.range(0, word.length())
                       .mapToLong(position ->
                                          this.computeDigitContribution(context, word, position))
                       .sum();
    }

    private long computeDigitContribution(SolverContext context, String word, int position) {
        char letter = word.charAt(position);
        int digit = context.valueByLetters()
                            .get(letter);

        int wordLength = word.length();
        long placeValue = computePlaceValueForPosition(wordLength, position);

        return digit * placeValue;
    }

    private long computePlaceValueForPosition(int wordLength, int position) {
        int power = wordLength - 1 - position;
        return (long) Math.pow(10, power);
    }
}
