public final class RollValidator {

    private static final int MAX_PINS = 10;
    private static final int MIN_PINS = 0;

    private static final String NEGATIVE_ROLL_MESSAGE = "Negative roll is invalid";
    private static final String TOO_MANY_PINS_MESSAGE = "Pin count exceeds pins on the lane";
    private static final String CANT_ROLL_AFTER_OVER_MESSAGE = "Cannot roll after game is over";

    public void validateRoll(final RollContext context) {
        this.validateGameNotOver(context);
        this.validatePinsNonNegative(context);
        this.validatePinsWithinMaximum(context);
        this.validateFrameRules(context);
    }

    private void validateGameNotOver(final RollContext context) {
        Frame frame = context.getFrame();
        boolean isGameOver = frame.isLastFrame() && frame.isComplete();
        if (isGameOver) {
            throw new IllegalStateException(CANT_ROLL_AFTER_OVER_MESSAGE);
        }
    }

    private void validatePinsNonNegative(final RollContext context) {
        if (context.getPins() < MIN_PINS) {
            throw new IllegalStateException(NEGATIVE_ROLL_MESSAGE);
        }
    }

    private void validatePinsWithinMaximum(final RollContext context) {
        if (context.getPins() > MAX_PINS) {
            throw new IllegalStateException(TOO_MANY_PINS_MESSAGE);
        }
    }

    private void validateFrameRules(final RollContext context) {
        Frame frame = context.getFrame();
        if (frame.isLastFrame()) {
            this.validateLastFrameRules(context);
            return;
        }

        this.validateStandardFrameRules(context);
    }

    private void validateStandardFrameRules(final RollContext context) {
        if (context.getRollIndex() == 0) {
            return;
        }

        if (this.isSecondRollExceedingMaxPins(context)) {
            throw new IllegalStateException(TOO_MANY_PINS_MESSAGE);
        }
    }

    private boolean isSecondRollExceedingMaxPins(final RollContext context) {
        Frame frame = context.getFrame();
        int totalPinsAfterSecondRoll = frame.firstRoll() + context.getPins();
        return totalPinsAfterSecondRoll > MAX_PINS;
    }

    private void validateLastFrameRules(final RollContext context) {
        int rollIndex = context.getRollIndex();
        if (rollIndex == 1) {
            this.validateSecondRollIfNeeded(context);
            return;
        }

        if (rollIndex == 2) {
            this.validateThirdRollIfNeeded(context);
            this.validateStrikeBonusIfNeeded(context);
        }
    }

    private void validateSecondRollIfNeeded(final RollContext context) {
        Frame frame = context.getFrame();
        if (frame.isStrike()) {
            return;
        }

        int totalPinsAfterSecondRoll = frame.firstRoll() + context.getPins();
        if (totalPinsAfterSecondRoll > MAX_PINS) {
            throw new IllegalStateException(TOO_MANY_PINS_MESSAGE);
        }
    }

    private void validateThirdRollIfNeeded(final RollContext context) {
        Frame frame = context.getFrame();
        boolean firstRollWasStrike = frame.isStrike();
        boolean firstTwoRollsWereSpare = !firstRollWasStrike && this.isFirstTwoRollsSpare(frame);

        if (!firstRollWasStrike && !firstTwoRollsWereSpare) {
            throw new IllegalStateException(TOO_MANY_PINS_MESSAGE);
        }
    }

    private boolean isFirstTwoRollsSpare(final Frame frame) {
        int totalPinsInFirstTwoRolls = frame.firstRoll() + frame.secondRoll();
        return totalPinsInFirstTwoRolls == MAX_PINS;
    }

    private void validateStrikeBonusIfNeeded(final RollContext context) {
        Frame frame = context.getFrame();
        boolean isThirdRoll = context.getRollIndex() == 2;
        if (!isThirdRoll || !frame.isStrike() || this.wasSecondRollStrike(frame)) {
            return;
        }

        int bonusPinsTotal = frame.secondRoll() + context.getPins();
        if (bonusPinsTotal > MAX_PINS) {
            throw new IllegalStateException(TOO_MANY_PINS_MESSAGE);
        }
    }

    private boolean wasSecondRollStrike(final Frame frame) {
        return frame.secondRoll() == MAX_PINS;
    }
}
