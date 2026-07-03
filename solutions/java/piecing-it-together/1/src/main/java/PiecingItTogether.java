public class PiecingItTogether {

    public static JigsawInfo getCompleteInformation(JigsawInfo input) {
        new Validator()
                .validate(input);

        PuzzleConfiguration constraintEvaluator = findUniqueMatchingConfiguration(input);

        if (constraintEvaluator.matchCount == 0) {
            throw new IllegalArgumentException("Insufficient data");
        }

        return constraintEvaluator.toJigsawInfo();
    }

    private static PuzzleConfiguration findUniqueMatchingConfiguration(final JigsawInfo input) {
        PuzzleConfiguration configuration = new PuzzleConfiguration(input);
        for (int row = 2; row <= 2000; row++) {
            for (int column = 2; column <= 2000; column++) {
                evaluate(configuration, new Position(row, column));
            }
        }

        return configuration;
    }

    private static void evaluate(
            final PuzzleConfiguration configuration,
            final Position position) {

        if (!configuration.violatesCoreConstraints(position)
            && !configuration.violatesDerivedConstraints(position)
            && !configuration.violatesFormatConstraint(position)
        ) {
            configuration.matchedRow = position.row();
            configuration.matchedColumn = position.column();
            configuration.matchCount++;

            if (configuration.matchCount > 1) {
                throw new IllegalArgumentException("Insufficient data");
            }
        }
    }
}