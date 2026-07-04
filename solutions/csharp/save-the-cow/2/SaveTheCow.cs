using System.Collections.Immutable;
using System.Diagnostics.CodeAnalysis;
using System.Reactive.Subjects;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not supported by Exercism")]
// ReSharper disable once CheckNamespace
public class TooManyGuessesException : Exception { }

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not supported by Exercism")]
// ReSharper disable once CheckNamespace
public sealed class GameState(string secretWord, string? maskedWord = null, int? remainingGuesses = null, ImmutableHashSet<char>? guessedChars = null)
{
    private const int DefaultGuesses = 9;
    public string MaskedWord { get; } = maskedWord ?? new('_', secretWord.Length);
    public ImmutableHashSet<char> GuessedChars { get; } = guessedChars ?? [];
    public int RemainingGuesses { get; } = remainingGuesses ?? DefaultGuesses;

    public bool IsGameWon => !maskedWord?.Contains('_') == true;
    public bool IsGameOver => this.IsGameWon || this.RemainingGuesses == 0;

    public bool TryEndingGame(char guessedCharacter, out GameState nextState)
    {
        nextState = this.GetNextState(guessedCharacter);
        return nextState.IsGameOver && nextState.IsGameWon;
    }

    private GameState GetNextState(char guessedCharacter)
    {
        string updatedMaskedWord = this.MaskedWord;
        int updatedRemainingGuesses = this.RemainingGuesses;
        if (this.IsCorrectNewGuess(guessedCharacter))
        {
            updatedMaskedWord = this.RevealLetters(this.MaskedWord, guessedCharacter);
        }
        else
        {
            updatedRemainingGuesses--;
        }

        ImmutableHashSet<char> updatedGuessedCharacters = this.GuessedChars.Add(guessedCharacter);
        return new(secretWord, updatedMaskedWord, updatedRemainingGuesses, updatedGuessedCharacters);
    }

    private bool IsCorrectNewGuess(char guessedCharacter)
    {
        bool secretWordContainsGuess = secretWord.Contains(guessedCharacter);
        bool guessAlreadyExists = this.GuessedChars.Contains(guessedCharacter);

        bool isValidNewCorrectGuess = secretWordContainsGuess && !guessAlreadyExists;
        return isValidNewCorrectGuess;
    }

    private string RevealLetters(string maskedWord, char guessedCharacter)
    {
        char[] chars = maskedWord.ToCharArray();

        for (int i = 0; i < secretWord.Length; i++)
        {
            if (secretWord[i] == guessedCharacter)
            {
                chars[i] = guessedCharacter;
            }
        }

        return new(chars);
    }
}

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not supported by Exercism")]
// ReSharper disable once CheckNamespace
public class SaveTheCow
{
    private readonly BehaviorSubject<GameState> _gameState;
    private readonly Subject<char> _guess = new();
    public SaveTheCow(string secretWord)
    {
        GameState initialState = new(secretWord);
        this._gameState = new(initialState);
        _ = this._guess.Subscribe(this.HandleGuess);
    }

    public IObservable<GameState> StateObservable => this._gameState;
    public IObserver<char> GuessObserver => this._guess;

    private void HandleGuess(char guessedCharacter)
    {
        GameState currentState = this._gameState.Value;
        if (currentState.IsGameOver)
        {
            if (!currentState.IsGameWon)
            {
                this._gameState.OnError(new TooManyGuessesException());
            }

            return;
        }

        ;
        bool isGameOver = currentState.TryEndingGame(guessedCharacter, out GameState nextState);
        if (isGameOver)
        {
            if (nextState.IsGameWon)
            {
                this._gameState.OnCompleted();
            }

            return;
        }

        this._gameState.OnNext(nextState);
    }
}