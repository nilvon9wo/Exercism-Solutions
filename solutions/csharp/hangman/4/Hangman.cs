using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Reactive;
using System.Reactive.Subjects;

// ReSharper disable once CheckNamespace
public class Hangman
{
	public IObservable<HangmanState> StateObservable { get; }
	public IObserver<char> GuessObserver { get; }

	public Hangman(string word)
	{
		BehaviorSubject<HangmanState> stateSubject = new(HangmanState.From(word));
		StateObservable = stateSubject;
		GuessObserver = Observer.Create<char>(
			newGuess =>
			{
				ImmutableHashSet<char> oldGuesses = stateSubject.Value.GuessedChars;
				bool isHit = IsCorrectGuess(word, oldGuesses, newGuess);
				ImmutableHashSet<char> newGuesses = oldGuesses.Add(newGuess);

				string maskedWord = word.Mask(newGuesses);
				if (maskedWord == word)
				{
					stateSubject.OnCompleted();
				}
				else if (stateSubject.Value.HasNoGuessesRemaining)
				{
					stateSubject.OnError(new TooManyGuessesException());
				}
				else
				{
					stateSubject.OnNext(stateSubject.Value.Update(maskedWord, newGuesses, isHit));
				}
			}
		);
	}

	private static bool IsCorrectGuess(string word, IReadOnlySet<char> oldGuesses, char newGuess)
		=> !oldGuesses.Contains(newGuess)
		   && word.Contains(newGuess, StringComparison.InvariantCulture);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public sealed class HangmanState
{
	public string MaskedWord { get; }
	public ImmutableHashSet<char> GuessedChars { get; }
	public int RemainingGuesses { get; }

	public bool HasNoGuessesRemaining
		=> RemainingGuesses <= 0;

	private HangmanState(string maskedWord, ImmutableHashSet<char> guessedChars, int remainingGuesses)
	{
		MaskedWord = maskedWord;
		GuessedChars = guessedChars;
		RemainingGuesses = remainingGuesses;
	}

	private const int _maxGuessCount = 9;

	public static HangmanState From(string word)
		=> new(
			word.Mask(new HashSet<char>()),
			ImmutableHashSet<char>.Empty,
			_maxGuessCount
		);

	public HangmanState Update(
		string maskedWord,
		ImmutableHashSet<char> guessedCharacters,
		bool isHit
	)
	{
		int guessDifference = isHit
			? 0
			: 1;
		int remainingGuesses = RemainingGuesses - guessDifference;
		return new(maskedWord, guessedCharacters, remainingGuesses);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class StringExtensions
{
	private const char _hidingChar = '_';

	internal static string Mask(this string word, IReadOnlySet<char> guessedChars)
		=> string.Concat(
			word.Select(
				correctCharacter => guessedChars.Contains(correctCharacter)
					? correctCharacter
					: _hidingChar
			)
		);
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