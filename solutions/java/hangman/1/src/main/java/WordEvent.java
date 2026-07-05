@SuppressWarnings("ClassCanBeRecord")
final class WordEvent implements Event {
    final String word;

    WordEvent(String word) {
        this.word = word;
    }
}
