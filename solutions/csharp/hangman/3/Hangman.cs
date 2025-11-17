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
	private readonly HangmanLogic _hangmanLogic;
	private readonly HangmanNotifier _hangmanNotifier;
	private HangmanState _currentState;

	public Hangman(string word)
	{
		HangmanStateFactory stateFactory = new(word);
		_hangmanLogic = new(word, stateFactory);
		_hangmanNotifier = new(word);
		_currentState = stateFactory.CreateFrom();
	}

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
		_hangmanNotifier.Notify(observer, _currentState);
		return Disposable.Empty;
	}

	public void OnNext(char value)
	{
		_currentState = _hangmanLogic.MakeGuess(_currentState, value);
		_hangmanNotifier.Notify(_observers, _currentState);
	}

	public void OnCompleted()
		=> throw new NotImplementedException();

	public void OnError(Exception error)
		=> throw new NotImplementedException();
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal class HangmanLogic
{
	private readonly string _word;
	private readonly HangmanStateFactory _stateFactory;

	public HangmanLogic(string word, HangmanStateFactory stateFactory)
	{
		_word = word;
		_stateFactory = stateFactory;
	}

	public HangmanState MakeGuess(HangmanState currentState, char letter)
	{
		bool isMatched = IsInWord(currentState, letter);
		int remainingGuesses = CalculateRemainingGuesses(currentState, isMatched);
		return _stateFactory.UpdateWith(currentState, letter, remainingGuesses);
	}

	private bool IsInWord(HangmanState currentState, char letter)
		=> _word.Contains(letter, StringComparison.InvariantCulture) && !currentState.GuessedChars.Contains(letter);

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
internal class HangmanNotifier
{
	private readonly string _word;

	public HangmanNotifier(string word)
		=> _word = word;

	public void Notify(List<IObserver<HangmanState>> observers, HangmanState currentState)
	{
		foreach (IObserver<HangmanState>? observer in observers)
		{
			Notify(observer, currentState);
		}
	}

	public void Notify(IObserver<HangmanState> observer, HangmanState currentState)
	{
		if (currentState.RemainingGuesses <= 0)
		{
			observer.OnError(new TooManyGuessesException());
		}
		else if (IsWon(currentState))
		{
			observer.OnCompleted();
		}
		else
		{
			observer.OnNext(currentState);
		}
	}

	private bool IsWon(HangmanState currentState)
		=> _word == currentState.MaskedWord;
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class MaskHandler
{
	private const char _mask = '_';

	internal static string Mask(this string word, ImmutableHashSet<char> guessedChars)
		=> word.Aggregate(
				new StringBuilder(),
				(maskedWord, c) => maskedWord.Append(Mask(c, guessedChars))
			)
			.ToString();

	private static char Mask(char c, ImmutableHashSet<char> guessedChars)
		=> guessedChars.Contains(c)
			? c
			: _mask;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class HangmanState
{
	public string MaskedWord { get; }
	public ImmutableHashSet<char> GuessedChars { get; }
	public int RemainingGuesses { get; }

	public HangmanState(string maskedWord, ImmutableHashSet<char> guessedChars, int remainingGuesses)
	{
		MaskedWord = maskedWord;
		GuessedChars = guessedChars;
		RemainingGuesses = remainingGuesses;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal class HangmanStateFactory
{
	private const int _initialGuesses = 9;
	private readonly string _word;

	public HangmanStateFactory(string word)
		=> _word = word;

	public HangmanState CreateFrom()
	{
		ImmutableHashSet<char> guessedCharacters = ImmutableHashSet<char>.Empty;
		string maskedWord = _word.Mask(guessedCharacters) ?? throw new ArgumentNullException(nameof(_word));
		return string.IsNullOrWhiteSpace(_word)
			? throw new global::System.ArgumentException("Value cannot be null or whitespace.", nameof(_word))
			: new(maskedWord, guessedCharacters, _initialGuesses);
	}

	public HangmanState UpdateWith(HangmanState currentState, char letter, int remainingGuesses)
	{
		ImmutableHashSet<char> guessedCharacters = currentState.GuessedChars.Add(letter);
		string maskedWord = _word.Mask(guessedCharacters);
		return new(maskedWord, guessedCharacters, remainingGuesses);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class TooManyGuessesException : Exception
{
	public TooManyGuessesException(string message)
		: base(message)
	{
	}

	public TooManyGuessesException(string message, Exception innerException)
		: base(message, innerException)
	{
	}

	public TooManyGuessesException()
	{
	}
}

//=======================================================================