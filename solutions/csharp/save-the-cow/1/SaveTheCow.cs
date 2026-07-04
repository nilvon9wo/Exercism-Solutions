using System.Collections.Immutable;
using System.Diagnostics.CodeAnalysis;
using System.Reactive.Subjects;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not supported by Exercism")]
// ReSharper disable once CheckNamespace
public class TooManyGuessesException : Exception { }

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not supported by Exercism")]
// ReSharper disable once CheckNamespace
public class GameState(string secretWord, string? maskedWord = null, int? remainingGuesses = null, ImmutableHashSet<char>? guessedChars = null)
{
    private const int DefaultGuesses = 9;
    public string MaskedWord { get; } = maskedWord ?? new('_', secretWord.Length);
    public ImmutableHashSet<char> GuessedChars { get; } = guessedChars ??  ImmutableHashSet<char>.Empty;
    public int RemainingGuesses { get; } = remainingGuesses ?? DefaultGuesses;
    
    public bool IsGameOver { get;  private set; }
    public bool IsGameWon { get;  private set; }

    public GameState GetNextState(char guessedCharacter)
    {
        (string updatedMaskedWord, int updatedRemainingGuesses) = CalculateUpdates(guessedCharacter);
        ImmutableHashSet<char> updatedGuessedCharacters = GuessedChars.Add(guessedCharacter);
        return new(secretWord, updatedMaskedWord, updatedRemainingGuesses, updatedGuessedCharacters); 
    }
    
    private (string updatedMaskedWord, int updatedRemainingGuesses) CalculateUpdates(char guessedCharacter)
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

        return (updatedMaskedWord, updatedRemainingGuesses);
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
    
    public bool EvaluateProgress()
    {
        bool isWin = !MaskedWord.Contains('_');
        bool isLose = !isWin && RemainingGuesses == 0;

        if (isLose)
        {
            this.IsGameOver = true;
        }

        if (!isWin)
        {
            return false;
        }

        this.CompleteGame();
        return true;
    }

    private void CompleteGame()
    {
        this.IsGameOver = true;
        this.IsGameWon = true;
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

        GameState nextState = currentState.GetNextState(guessedCharacter);
        bool isGameOver = nextState.EvaluateProgress();
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