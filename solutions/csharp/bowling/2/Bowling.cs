using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class BowlingGame
{
	private readonly Frames _frames = new();

	public void Roll(int pins)
		=> _ = _frames.AddRoll(pins);

	public int Score()
	{
		if (!_frames.AreComplete)
		{
			throw new ArgumentException("An incomplete game cannot be scored.");
		}

		if (_frames.ArePerfect)
		{
			return 300;
		}

		int score = 0;
		for (int frameIndex = 0; frameIndex < _frames.Count; frameIndex++)
		{
			Frame currentFrame = _frames[frameIndex];

			if (currentFrame.IsStrike)
			{
				score += 10 + GetStrikeBonus(currentFrame, frameIndex);
			}
			else if (currentFrame.IsSpare)
			{
				score += 10 + GetSpareBonus(currentFrame, frameIndex);
			}
			else
			{
				score += GetNormalFrameScore(currentFrame);
			}
		}

		return score;
	}

	private static int GetNormalFrameScore(Frame currentFrame)
		=> currentFrame.RollOne + (currentFrame.RollTwo ?? 0);

	private int GetSpareBonus(Frame currentFrame, int frameIndex)
	{
		if (currentFrame.IsLastFrame)
		{
			return currentFrame.BonusRollOne ?? 0;
		}

		Frame nextFrame = _frames[frameIndex + 1];
		return nextFrame.RollOne;
	}

	private int GetStrikeBonus(Frame currentFrame, int frameIndex)
	{
		if (_frames.IsFinalFrame(frameIndex))
		{
			return GetLastFrameBonus(currentFrame);
		}

		Frame nextFrame = _frames[frameIndex + 1];
		return nextFrame.IsStrike
			? CalculateStrikeBonus(frameIndex)
			: CalculateNonStrikeBonus(nextFrame, frameIndex);
	}

	private int CalculateStrikeBonus(int frameIndex)
		=> (frameIndex + 2) < _frames.Count
			? 10 + CalculateThirdFrameBonus(_frames[frameIndex + 2])
			: 10;

	private int CalculateNonStrikeBonus(Frame nextFrame, int frameIndex)
	{
		int bonus = GetNormalFrameScore(nextFrame);
		if ((frameIndex + 2) >= _frames.Count)
		{
			return bonus;
		}

		Frame thirdFrame = _frames[frameIndex + 2];
		if (!thirdFrame.IsStrike)
		{
			bonus += thirdFrame.RollOne + (thirdFrame.RollTwo ?? 0);
		}

		return bonus;
	}

	private int GetLastFrameBonus(Frame currentFrame)
	{
		int bonus = currentFrame.BonusRollOne ?? 0;
		if (currentFrame.BonusRollTwo != null)
		{
			bonus += currentFrame.BonusRollTwo.Value;
		}

		if (!currentFrame.IsStrike
			|| (currentFrame.BonusRollOne == null)
			|| (currentFrame.BonusRollTwo != null))
		{
			return bonus;
		}

		bonus += 10;

		if (_frames.Count > 1)
		{
			bonus += _frames[^2].RollOne;
		}

		return bonus;
	}

	private static int CalculateThirdFrameBonus(Frame thirdFrame)
		=> thirdFrame.IsStrike
			? 10
			: thirdFrame.RollOne;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public record Frame
{
	private const int _strike = 10;

	private readonly int? _rollOne;

	public int RollOne
	{
		get
			=> (int)_rollOne!;
		init
			=> _rollOne = ValidateRoll(value);
	}

	public int? RollTwo { get; private set; }

	public int? BonusRollOne { get; private set; }

	public int? BonusRollTwo { get; private set; }

	public bool IsLastFrame { get; init; }

	public Frame AddRoll(int pins)
	{
		if (!IsLastFrame
			|| ((_rollOne != _strike) && !IsSpare))
		{
			RollTwo = ValidateSecondRoll(pins);
		}
		else if ((BonusRollOne == null) || IsSpare)
		{
			BonusRollOne = ValidateBonusRollOne(pins);
		}
		else
		{
			BonusRollTwo = ValidateBonusRollTwo(pins);
		}

		return this;
	}

	private int? ValidateSecondRoll(int? pins)
	{
		_ = ValidateRoll(pins);
		switch (_rollOne)
		{
			case _strike:
				throw new ArgumentException("No second rolls allowed when first roll is strike.", nameof(pins));

			default:
				if ((_rollOne + pins) > _strike)
				{
					throw new ArgumentException(
						$"First and second roll cannot add up to more than {_strike} pins.",
						nameof(pins)
					);
				}

				break;
		}

		return pins;
	}

	private int? ValidateBonusRollOne(int? pins)
		=> !IsLastFrame && (_rollOne == _strike)
			? throw new InvalidOperationException("Bonus rolls only allowed when first roll of last frame is a strike.")
			: ValidateRoll(pins);

	private int? ValidateBonusRollTwo(int? pins)
	{
		_ = ValidateRoll(pins);
		return BonusRollTwo != null
			? throw new InvalidOperationException("No more bonus rolls left.")
			: BonusRollOne == null
				? throw new InvalidOperationException("First bonus roll has not been rolled yet.")
				: BonusRollOne == _strike
					? pins
					: (BonusRollOne + pins) switch
					{
						> _strike => throw new ArgumentException(
							$"First and second bonus rolls cannot add up to more than {_strike} pins.",
							nameof(pins)
						),
						_ => pins,
					};
	}

	private static int? ValidateRoll(int? pins)
	{
		_ = pins ?? throw new ArgumentNullException(nameof(pins));
		return pins switch
		{
			< 0
				=> throw new ArgumentException("It is impossible to roll less than 0.", nameof(pins)),

			> _strike
				=> throw new ArgumentException($"It is impossible to roll more than {_strike}.", nameof(pins)),
			_
				=> pins,
		};
	}

	public bool IsStrike
		=> RollOne == _strike;

	public bool IsSpare
		=> !IsStrike
		   && RollTwo.HasValue
		   && ((RollOne + RollTwo.Value) == _strike);

	public bool IsComplete
		=> IsLastFrame
			? RollOne == _strike
				? BonusRollTwo.HasValue
				: IsSpare
					? BonusRollOne.HasValue
					: RollTwo.HasValue
			: IsStrike || RollTwo.HasValue;

	public bool IsPerfect
		=> (RollOne == _strike)
		   && (BonusRollOne == _strike)
		   && (BonusRollTwo == _strike);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal class Frames
{
	private const int _finalFrame = 10;
	private readonly List<Frame> _frames = new();

	private Frame? _lastFrame
		=> _frames.Count == _finalFrame
			? _frames[_finalFrame - 1]
			: null;

	public int Count
		=> _frames.Count;

	public Frames AddRoll(int pins)
	{
		if (AreComplete)
		{
			throw new ArgumentException("A complete game cannot receive more rolls.", nameof(pins));
		}

		Frame? lastFrame = _frames.LastOrDefault();
		if (lastFrame?.IsComplete != false)
		{
			_frames.Add(new() { RollOne = pins, IsLastFrame = _frames.Count == 9 });
		}
		else
		{
			_ = lastFrame.AddRoll(pins);
		}

		return this;
	}

	internal Frame this[int frameIndex]
		=> _frames[frameIndex];

	internal bool AreComplete
		=> _lastFrame?.IsComplete == true;

	internal bool IsFinalFrame(int frameIndex)
		=> frameIndex == (Count - 1);

	internal bool ArePerfect
		=> AreComplete
		   && _frames.All(frame => frame.IsStrike)
		   && (_lastFrame?.IsPerfect == true);
}

//=======================================================================