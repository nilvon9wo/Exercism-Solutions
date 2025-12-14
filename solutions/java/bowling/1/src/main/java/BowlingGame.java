import java.util.ArrayList;
import java.util.List;

public final class BowlingGame {

    private static final int MAX_FRAMES = 10;

    private final RollValidator rollValidator;
    private final ScoreCalculator scoreCalculator;

    private final List<Frame> frames = new ArrayList<>();

    public BowlingGame() {
        this.rollValidator = new RollValidator();
        this.scoreCalculator = new ScoreCalculator();
    }

    private static final String SCORE_NOT_AVAILABLE_MESSAGE = ScoreCalculator.SCORE_NOT_AVAILABLE_MESSAGE;

    public void roll(final int pins) {
        Frame currentFrame = this.getOrCreateCurrentFrame();
        this.rollValidator.validateRoll(new RollContext(currentFrame, pins));
        currentFrame.addRoll(pins);
    }

    private Frame getOrCreateCurrentFrame() {
        if (this.frames.isEmpty()) {
            return this.createFrame(false);
        }

        Frame lastFrame = this.frames.getLast();
        if (!lastFrame.canAcceptRoll() && this.frames.size() < MAX_FRAMES) {
            boolean isLastFrame = this.isCreatingLastFrame();
            return this.createFrame(isLastFrame);
        }

        return lastFrame;
    }

    private boolean isCreatingLastFrame() {
        return this.frames.size() + 1 == MAX_FRAMES;
    }

    private Frame createFrame(final boolean isLastFrame) {
        Frame frame = new Frame(isLastFrame);
        this.frames.add(frame);
        return frame;
    }

    public int score() {
        this.validateGameIsComplete();
        return this.scoreCalculator.calculateScore(this.frames);
    }

    private void validateGameIsComplete() {
        if (this.frames.size() < MAX_FRAMES) {
            throw new IllegalStateException(SCORE_NOT_AVAILABLE_MESSAGE);
        }

        Frame lastFrame = this.frames.get(MAX_FRAMES - 1);
        if (lastFrame.isIncomplete()) {
            throw new IllegalStateException(SCORE_NOT_AVAILABLE_MESSAGE);
        }
    }
}
