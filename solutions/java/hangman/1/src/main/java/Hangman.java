import io.reactivex.Observable;

class Hangman {
    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        return Observable.merge(
                                 words.map(WordEvent::new),
                                 letters.map(LetterEvent::new)
                         )
                         .scan(new GameState(), GameState::apply)
                         .skip(1)
                         .map(GameState::toOutput);
    }
}