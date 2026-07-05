import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class GameState {

    private String secret;
    private final Set<String> guessed = new LinkedHashSet<>();
    private final Set<String> misses = new LinkedHashSet<>();
    private final List<Part> parts = new ArrayList<>();
    private Status status = Status.PLAYING;

    GameState apply(Event event) {
        if (event instanceof WordEvent we) {
            reset(we.word);
            return this;
        }

        if (status != Status.PLAYING) {
            return this;
        }

        LetterEvent letterEvent = (LetterEvent) event;
        if (guessed.contains(letterEvent.letter())
            || misses.contains(letterEvent.letter())) {
            throw new IllegalArgumentException("Letter " + letterEvent.letter() + " was already played");
        }

        if (secret.contains(letterEvent.letter())) {
            guessed.add(letterEvent.letter());
        }
        else {
            misses.add(letterEvent.letter());
            addPart();
        }

        updateStatus();
        return this;
    }

    private void reset(String word) {
        this.secret = word;
        guessed.clear();
        misses.clear();
        parts.clear();
        status = Status.PLAYING;
    }

    private void addPart() {
        Part[] order = Part.values();
        final int size = parts.size();
        if (size < order.length) {
            parts.add(order[size]);
        }
    }

    private void updateStatus() {
        boolean won = secret.chars()
                            .allMatch(character -> isContains((char) character));

        if (won) {
            status = Status.WIN;
            return;
        }

        if (parts.size() == Part.values().length) {
            status = Status.LOSS;
        }
    }

    private boolean isContains(final char character) {
        return guessed.contains(String.valueOf(character));
    }

    Output toOutput() {
        if (secret == null) {
            return Output.empty();
        }

        return new Output(
                secret,
                buildDiscovered(),
                guessed,
                misses,
                parts,
                status
        );
    }

    private String buildDiscovered() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char character : secret.toCharArray()) {
            stringBuilder.append(isContains(character)
                      ? character
                      : "_");
        }

        return stringBuilder.toString();
    }
}
