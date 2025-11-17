using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Reactive.Disposables;
using System.Reactive.Subjects;
using System.Text;

// ReSharper disable once CheckNamespace
public class Hangman : ISubject<char, HangmanState>
{
	private const int _initialGuesses = 9;
	private HangmanState _currentState;

	public Hangman(string word)
		=> _currentState = HangmanState.CreateFrom(word, _initialGuesses);

	public IObservable<HangmanState> StateObservable
		=> this;

	public IObserver<char> GuessObserver
		=> this;

	private readonly List<IObserver<HangmanState>> _observers = new();

	public IDisposable Subscribe(IObserver<HangmanState> observer)
	{
		if (observer == null)
		{
			throw new ArgumentNullException(nameof(observer));
		}

		_observers.Add(observer);
		observer.Notify(_currentState);
		return Disposable.Empty;
	}

	public void OnNext(char value)
	{
		_currentState = HangmanLogic.MakeGuess(_currentState, value);
		_observers.Notify(_currentState);
	}

	public void OnCompleted()
		=> throw new NotImplementedException();

	public void OnError(Exception error)
		=> throw new NotImplementedException();
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class HangmanLogic
{
	public static HangmanState MakeGuess(HangmanState currentState, char letter)
	{
		bool isMatched = currentState.IsInWord(letter);
		int remainingGuesses = CalculateRemainingGuesses(currentState, isMatched);
		return !isMatched
			   && remainingGuesses < 0
			? currentState.Finish()
			: currentState.UpdateWith(letter, remainingGuesses);
	}

	private static int CalculateRemainingGuesses(HangmanState currentState, bool isMatched)
	{
		int remainingGuesses = currentState.RemainingGuesses;
		return isMatched
			? remainingGuesses
			: remainingGuesses - 1;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class HangmanNotifier
{
	public static void Notify(this List<IObserver<HangmanState>> observers, HangmanState currentState)
	{
		foreach (IObserver<HangmanState>? observer in observers)
		{
			Notify(observer, currentState);
		}
	}

	public static void Notify(this IObserver<HangmanState> observer, HangmanState currentState)
	{
		if (currentState.IsWon)
		{
			observer.OnCompleted();
		}
		else if (currentState.IsTooManyGuesses)
		{
			observer.OnError(new TooManyGuessesException());
		}
		else
		{
			observer.OnNext(currentState);
		}
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class MaskHandler
{
	private const char _mask = '_';

	internal static string Mask(this string word, ImmutableHashSet<char> guessedChars)
		=> word.Aggregate(
				new StringBuilder(),
				(maskedWord, c) => maskedWord.Append(
					Mask(c, guessedChars)
				)
			)
			.ToString();

	private static char Mask(char c, ImmutableHashSet<char> guessedChars)
		=> guessedChars.Contains(c)
			? c
			: _mask;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public sealed class HangmanState
{
	private HangmanState()
	{
	}

	public required string Word { get; init; }
	public required string MaskedWord { get; init; }

	public required ImmutableHashSet<char> GuessedChars { get; init; }
	public required int RemainingGuesses { get; init; }

	public bool IsWon
		=> Word == MaskedWord;

	public bool IsTooManyGuesses { get; private init; }

	public bool IsInWord(char letter)
		=> Word.Contains(letter, StringComparison.InvariantCulture)
		   && !GuessedChars.Contains(letter);

	public static HangmanState CreateFrom(string word, int initialGuessCount)
	{
		ImmutableHashSet<char> guessedCharacters = ImmutableHashSet<char>.Empty;
		return string.IsNullOrWhiteSpace(word)
			? throw new ArgumentException("Value cannot be null or whitespace.", nameof(word))
			: new HangmanState()
			{
				Word = word,
				MaskedWord = word.Mask(guessedCharacters) ?? throw new ArgumentNullException(nameof(word)),
				GuessedChars = guessedCharacters,
				RemainingGuesses = initialGuessCount
			};
	}

	public HangmanState UpdateWith(char letter, int remainingGuesses)
	{
		ImmutableHashSet<char> guessedCharacters = GuessedChars.Add(letter);
		return new HangmanState
		{
			Word = Word,
			GuessedChars = guessedCharacters,
			MaskedWord = Word.Mask(guessedCharacters),
			RemainingGuesses = remainingGuesses
		};
	}

	public HangmanState Finish()
		=> new()
		{
			Word = Word,
			MaskedWord = MaskedWord,
			GuessedChars = GuessedChars,
			RemainingGuesses = RemainingGuesses,
			IsTooManyGuesses = true
		};
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class TooManyGuessesException : Exception
{
	public TooManyGuessesException(string message) : base(message)
	{
	}

	public TooManyGuessesException(string message, Exception innerException) : base(message, innerException)
	{
	}

	public TooManyGuessesException() : base()
	{
	}
}

//=======================================================================