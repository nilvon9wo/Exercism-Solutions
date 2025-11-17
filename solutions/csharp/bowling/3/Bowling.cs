using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class BowlingGame
{
    private const int _strike = Frame.Strike;
    private const int _perfectScore = 300;
    private readonly Frames _frames = new();

	public void Roll(int pins)
		=> _ = _frames.AddRoll(pins);

    public int Score() 
        => !_frames.AreComplete
            ? throw new ArgumentException("An incomplete game cannot be scored.")
            : _frames.ArePerfect
                ? _perfectScore
                : _frames.Aggregate(0, (score, currentFrame) =>
                {
                    score += currentFrame.IsStrike
                                ? _strike + GetStrikeBonus(currentFrame)
                                : currentFrame.IsSpare
                                    ? _strike + GetSpareBonus(currentFrame)
                                    : GetNormalFrameScore(currentFrame);
                    return score;
                });

    private static int GetNormalFrameScore(Frame? currentFrame)
		=> (currentFrame?.RollOne ?? 0) 
            + (currentFrame?.RollTwo ?? 0);

    private static int GetSpareBonus(Frame currentFrame) 
        => currentFrame.IsFinalFrame 
            ? currentFrame.BonusRollOne ?? 0 
            : currentFrame?.NextFrame?.RollOne ?? 0;

    private static int GetStrikeBonus(Frame currentFrame)
	{
		if (currentFrame.IsFinalFrame)
		{
			return GetLastFrameBonus(currentFrame);
		}

		Frame? nextFrame = currentFrame.NextFrame;
		return nextFrame?.IsStrike == true
			? CalculateSecondStrikeBonus(nextFrame)
			: CalculateSecondNonStrikeBonus(nextFrame);
	}

	private static int CalculateSecondStrikeBonus(Frame secondFrame)
    {
        Frame? thirdFrame = secondFrame?.NextFrame;
        return (thirdFrame != null)
			? _strike + CalculateThirdFrameBonus(thirdFrame)
			: _strike;
    }

	private static int CalculateSecondNonStrikeBonus(Frame? secondFrame)
	{
        Frame? thirdFrame = secondFrame?.NextFrame;
		int bonus = GetNormalFrameScore(secondFrame);
        return thirdFrame == null
            ? bonus
            : (!thirdFrame.IsStrike)
                ? bonus + GetNormalFrameScore(thirdFrame)
                : bonus;
    }

    private static int GetLastFrameBonus(Frame currentFrame) 
        => (currentFrame.BonusRollOne ?? 0)
            + (currentFrame.BonusRollTwo ?? 0);

    private static int CalculateThirdFrameBonus(Frame thirdFrame)
		=> thirdFrame.IsStrike
			? _strike
			: thirdFrame.RollOne;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public record Frame
{
	public const int Strike = 10;

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

    public Frame? NextFrame { get; set; }
	public bool IsFinalFrame { get; init; }

	public Frame AddRoll(int pins)
	{
		if (!IsFinalFrame
			|| ((_rollOne != Strike) && !IsSpare))
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
			case Strike:
				throw new ArgumentException("No second rolls allowed when first roll is strike.", nameof(pins));

			default:
				if ((_rollOne + pins) > Strike)
				{
					throw new ArgumentException(
						$"First and second roll cannot add up to more than {Strike} pins.",
						nameof(pins)
					);
				}

				break;
		}

		return pins;
	}

	private int? ValidateBonusRollOne(int? pins)
		=> !IsFinalFrame && (_rollOne == Strike)
			? throw new InvalidOperationException("Bonus rolls only allowed when first roll of last frame is a strike.")
			: ValidateRoll(pins);

	private int? ValidateBonusRollTwo(int? pins)
	{
		_ = ValidateRoll(pins);
		return BonusRollTwo != null
			? throw new InvalidOperationException("No more bonus rolls left.")
			: BonusRollOne == null
				? throw new InvalidOperationException("First bonus roll has not been rolled yet.")
				: BonusRollOne == Strike
					? pins
					: (BonusRollOne + pins) switch
					{
						> Strike => throw new ArgumentException(
							$"First and second bonus rolls cannot add up to more than {Strike} pins.",
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

			> Strike
				=> throw new ArgumentException($"It is impossible to roll more than {Strike}.", nameof(pins)),
			_
				=> pins,
		};
	}

	public bool IsStrike
		=> RollOne == Strike;

	public bool IsSpare
		=> !IsStrike
		   && RollTwo.HasValue
		   && ((RollOne + RollTwo.Value) == Strike);

	public bool IsComplete
		=> IsFinalFrame
			? RollOne == Strike
				? BonusRollTwo.HasValue
				: IsSpare
					? BonusRollOne.HasValue
					: RollTwo.HasValue
			: IsStrike || RollTwo.HasValue;

	public bool IsPerfect
		=> (RollOne == Strike)
		   && (BonusRollOne == Strike)
		   && (BonusRollTwo == Strike);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal class Frames : IEnumerable<Frame>
{
	private const int _totalFrameCount = 10;
	private const int _finalFrameIndex = _totalFrameCount - 1;
	private readonly List<Frame> _frames = new();

	private Frame? _finalFrame
		=> _frames.Count == _totalFrameCount
			? _frames[_finalFrameIndex]
			: null;

	public Frames AddRoll(int pins)
	{
		if (AreComplete)
		{
			throw new ArgumentException("A complete game cannot receive more rolls.", nameof(pins));
		}

		Frame? lastFrame = _frames.LastOrDefault();
		if (lastFrame?.IsComplete != false)
        {
            Frame newFrame = new() { RollOne = pins, IsFinalFrame = IsPreviousFramePenultimateFrame() };
            if (lastFrame != null)
            {
                lastFrame.NextFrame = newFrame;
            }

            _frames.Add(newFrame);
        }
        else
		{
			_ = lastFrame.AddRoll(pins);
		}

		return this;
	}

    private bool IsPreviousFramePenultimateFrame() 
        => _frames.Count == _totalFrameCount - 1;

    internal Frame this[int frameIndex]
		=> _frames[frameIndex];

	internal bool AreComplete
		=> (_frames.Count == _totalFrameCount)
            && _finalFrame?.IsComplete == true;

	internal bool ArePerfect
		=> AreComplete
		   && _frames.All(frame => frame.IsStrike)
		   && (_finalFrame?.IsPerfect == true);

     public IEnumerator<Frame> GetEnumerator()
    {
        Frame? currentFrame = _frames[0] ?? throw new InvalidOperationException("No frames yet!");
        while (currentFrame != null)
        {
            yield return currentFrame!;
            currentFrame = currentFrame!.NextFrame;
        }
    }

    IEnumerator IEnumerable.GetEnumerator()
        => GetEnumerator();
}

//=======================================================================