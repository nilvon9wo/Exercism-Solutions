import java.util.List;
import java.util.stream.IntStream;

public final class FrameScorer {

    private static final int STRIKE_PINS = 10;

    public int scoreFrame(final FrameContext context) {
        Frame frame = context.getCurrentFrame();
        boolean isLastFrame = context.isLastFrame();

        if (frame.isStrike()) {
            return isLastFrame
                   ? scoreLastFrameStrike(frame)
                   : scoreStrike(frame, context);
        }

        if (frame.isSpare()) {
            return isLastFrame
                   ? scoreLastFrameSpare(frame)
                   : scoreSpare(frame, context);
        }

        return frame.pinsTotal();
    }

    private int scoreLastFrameSpare(final Frame frame) {
        // The third roll is the bonus for spare in the last frame
        List<Integer> rolls = frame.getRolls();
        int bonusRoll = rolls.size() > 2 ? rolls.get(2) : 0;
        return STRIKE_PINS + bonusRoll;
    }

    private int scoreLastFrameStrike(final Frame frame) {
        // Last frame strike can have up to two bonus rolls
        List<Integer> rolls = frame.getRolls();
        int secondRoll = rolls.size() > 1 ? rolls.get(1) : 0;
        int thirdRoll = rolls.size() > 2 ? rolls.get(2) : 0;
        return STRIKE_PINS + secondRoll + thirdRoll;
    }

    private int scoreStrike(final Frame frame, final FrameContext context) {
        int bonus = calculateStrikeBonus(context);
        return STRIKE_PINS + bonus;
    }

    private int scoreSpare(final Frame frame, final FrameContext context) {
        int bonus = calculateSpareBonus(context);
        return STRIKE_PINS + bonus;
    }

    private int calculateSpareBonus(final FrameContext context) {
        Frame nextFrame = context.getNextFrame();
        return nextFrame != null ? nextFrame.firstRoll() : 0;
    }

    private int calculateStrikeBonus(final FrameContext context) {
        Frame nextFrame = context.getNextFrame();
        if (nextFrame == null) return 0;

        Frame followingFrame = context.getFollowingFrame();
        boolean nextFrameIsStrike = nextFrame.isStrike();
        boolean hasFollowingFrame = followingFrame != null;

        if (nextFrameIsStrike && hasFollowingFrame) {
            return sumFirstRolls(nextFrame, followingFrame);
        }

        return sumFirstNRolls(nextFrame.getRolls(), 2);
    }

    private int sumFirstRolls(final Frame firstFrame, final Frame secondFrame) {
        return sumFirstNRolls(List.of(firstFrame.firstRoll(), secondFrame.firstRoll()), 2);
    }

    private int sumFirstTwoRolls(final List<Integer> rolls) {
        return sumFirstNRolls(rolls, 2);
    }

    private int sumFirstNRolls(final List<Integer> rolls, final int count) {
        return IntStream.range(0, count)
                        .map(i -> getRollOrZero(rolls, i))
                        .sum();
    }

    private static int getRollOrZero(final List<Integer> rolls, final int index) {
        return index < rolls.size() ? rolls.get(index) : 0;
    }
}
