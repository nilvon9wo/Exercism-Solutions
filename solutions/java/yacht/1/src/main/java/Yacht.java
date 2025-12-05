record Yacht(YachtCategoryScorer categoryScorer, YachtCategory yachtCategory) {

    Yacht(int[] diceValues, YachtCategory yachtCategory) {
        this(new YachtCategoryScorer(diceValues), yachtCategory);
    }

    private static final int YACHT_SCORE = 50;
    private static final int STRAIGHT_SCORE = 30;

    int score() {
        return switch (this.yachtCategory) {
            case YACHT -> this.categoryScorer.scoreYacht(YACHT_SCORE);
            case ONES -> this.categoryScorer.scoreNumber(1);
            case TWOS -> this.categoryScorer.scoreNumber(2);
            case THREES -> this.categoryScorer.scoreNumber(3);
            case FOURS -> this.categoryScorer.scoreNumber(4);
            case FIVES -> this.categoryScorer.scoreNumber(5);
            case SIXES -> this.categoryScorer.scoreNumber(6);
            case FULL_HOUSE -> this.categoryScorer.scoreFullHouse();
            case FOUR_OF_A_KIND -> this.categoryScorer.scoreFourOfAKind();
            case LITTLE_STRAIGHT -> this.categoryScorer.scoreStraight(1, STRAIGHT_SCORE);
            case BIG_STRAIGHT -> this.categoryScorer.scoreStraight(2, STRAIGHT_SCORE);
            case CHOICE -> this.categoryScorer.sumDice();
        };
    }
}




