using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Runtime.Serialization;

// ReSharper disable once CheckNamespace
public class TwoBucket
{
	private readonly BucketState _state;

	public TwoBucket(int bucketOneCapacity, int bucketTwoCapacity, Bucket startBucket)
		=> _state = new(bucketOneCapacity, bucketTwoCapacity, startBucket);

	public TwoBucketResult Measure(int goal)
	{
		BucketState state = _state.SetGoal(goal);
		try
		{
			return Measure(state);
		}
		catch (NoSolutionFoundException ex)
		{
			throw new ArgumentException(ex.Message, nameof(goal));
		}
	}

	private static TwoBucketResult Measure(BucketState state)
	{
		state = DoFirstFill(state);
		Result<BucketState> result = FindGoal(state);
		return result.IsValid
			? TwoBucketResult.From(result.Value)
			: throw result.Exception;
	}

	private static BucketState DoFirstFill(BucketState state)
	{
		BucketAction action = state.StartBucket == Bucket.One
			? BucketAction.FillBucketOne
			: BucketAction.FillBucketTwo;
		return action.Execute(state);
	}

	private const int _maxSearchDepth = 100;

	private static Result<BucketState> FindGoal(
		BucketState oldState,
		HashSet<BucketState>? visitedStates = null,
		int depth = 0
	)
	{
		if (oldState.HasReachedGoal)
		{
			return Result<BucketState>.From(oldState);
		}

		if (depth > _maxSearchDepth)
		{
			return Result<BucketState>.From(NoSolutionFoundException.From(oldState));
		}

		visitedStates ??= new();
		if (visitedStates.Contains(oldState))
		{
			return Result<BucketState>.From(RepetitionException.From(oldState));
		}

		_ = visitedStates.Add(oldState);
		List<BucketState> successfulResults = CollectSuccessfulStates(oldState, visitedStates, depth);
		return DetermineResult(successfulResults, oldState);
	}

	private static List<BucketState> CollectSuccessfulStates(
		BucketState oldState,
		HashSet<BucketState>? visitedStates,
		int depth
	)
	{
		List<BucketState> successfulResults = new();
		successfulResults.AddRange(
			from BucketState? newState in GetPossibleNewStates(oldState)
			let exploredStateResult = FindGoal(
				newState,
				visitedStates,
				depth + 1
			)
			where exploredStateResult.IsValid
			select exploredStateResult.Value
		);
		return successfulResults;
	}

	private static Result<BucketState> DetermineResult(
		IReadOnlyCollection<BucketState> successfulResults,
		BucketState oldState
	)
	{
		switch (successfulResults.Count)
		{
			case > 0:
				{
					BucketState? bestResult = successfulResults.MinBy(x => x.Moves);
					return Result<BucketState>.From(bestResult!);
				}

			default:
				return Result<BucketState>.From(NoSolutionFoundException.From(oldState));
		}
	}

	[SuppressMessage(
		"Style",
		"IDE2006:Blank line not allowed after arrow expression clause token",
		Justification = "Conflicting code style rules"
	)]
	private static IEnumerable<BucketState> GetPossibleNewStates(BucketState oldState)
		=>
			from BucketAction action in BucketActionProvider.GetAvailableActions(oldState)
			let newState = action.Execute(oldState)
			select newState;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class EmptyBucketOneStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket = bucketState[Bucket.One]
			.Empty();
		return bucketState.Update(bucket);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class EmptyBucketTwoStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket = bucketState[Bucket.Two]
			.Empty();
		return bucketState.Update(bucket);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class FillBucketOneStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket = bucketState[Bucket.One]
			.Fill();
		return bucketState.Update(bucket);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class FillBucketTwoStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket = bucketState[Bucket.Two]
			.Fill();
		return bucketState.Update(bucket);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public interface IMeasurementStep
{
	BucketState Execute(BucketState bucketState);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class MeasurementStepLookup
{
	private static readonly Dictionary<BucketAction, Type> _stepTypeByActions = new()
	{
		{ BucketAction.None, typeof(NoOpStep) },
		{ BucketAction.PourBucketOneIntoBucketTwo, typeof(PourBucketFromOneToTwoStep) },
		{ BucketAction.PourBucketTwoIntoBucketOne, typeof(PourBucketFromTwoToOneStep) },
		{ BucketAction.EmptyBucketOne, typeof(EmptyBucketOneStep) },
		{ BucketAction.EmptyBucketTwo, typeof(EmptyBucketTwoStep) },
		{ BucketAction.FillBucketOne, typeof(FillBucketOneStep) },
		{ BucketAction.FillBucketTwo, typeof(FillBucketTwoStep) },
	};

	private static readonly Dictionary<BucketAction, IMeasurementStep> _stepByActions = new();

	internal static BucketState Execute(this BucketAction bucketAction, BucketState state)
		=> Get(bucketAction)
			.Execute(state);

	private static IMeasurementStep Get(BucketAction bucketAction)
	{
		if (!_stepByActions.TryGetValue(
				bucketAction,
				out IMeasurementStep? _
			))
		{
			_stepByActions[bucketAction] = _stepTypeByActions.TryGetValue(
				bucketAction,
				out Type? type
			)
				? (IMeasurementStep)Activator.CreateInstance(
					type
				)!
				: throw new ArgumentException(
					"{bucketAction} is not supported.",
					nameof(bucketAction)
				);
		}

		return _stepByActions[bucketAction];
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public class NoOpStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
		=> bucketState;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class PourBucketFromOneToTwoStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket1 = bucketState[Bucket.One];
		BucketInformation bucket2 = bucketState[Bucket.Two];

		int bucket1Holding = bucket1.HoldingInLiters;
		int bucket2Unused = bucket2.UnusedInLiters;
		return bucket2Unused >= bucket1Holding
			? bucketState.Update(
				new[]
				{
					bucket1.RemoveContent(bucket1Holding),
					bucket2.AddContent(bucket1Holding),
				}
			)
			: bucketState.Update(
				new[]
				{
					bucket1.RemoveContent(bucket2Unused),
					bucket2.AddContent(bucket2Unused),
				}
			);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public class PourBucketFromTwoToOneStep : IMeasurementStep
{
	public BucketState Execute(BucketState bucketState)
	{
		_ = bucketState ?? throw new ArgumentNullException(nameof(bucketState));
		BucketInformation bucket1 = bucketState[Bucket.One];
		BucketInformation bucket2 = bucketState[Bucket.Two];

		int bucket2Holding = bucket2.HoldingInLiters;
		int bucket1Unused = bucket1.UnusedInLiters;
		return bucket1Unused >= bucket2Holding
			? bucketState.Update(
				new[]
				{
					bucket1.AddContent(bucket2Holding),
					bucket2.RemoveContent(bucket2Holding),
				}
			)
			: bucketState.Update(
				new[]
				{
					bucket1.AddContent(bucket1Unused),
					bucket2.RemoveContent(bucket1Unused),
				}
			);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public enum Bucket
{
	One,
	Two,
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal enum BucketAction
{
	None,
	PourBucketOneIntoBucketTwo,
	PourBucketTwoIntoBucketOne,
	EmptyBucketOne,
	EmptyBucketTwo,
	FillBucketOne,
	FillBucketTwo,
	QuitImpossibleTask,
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class BucketActionProvider
{
	// After an action, you may not arrive at a state where the starting bucket is empty and the other bucket is full.
	internal static HashSet<BucketAction> GetAvailableActions(BucketState state)
	{
		HashSet<BucketAction> availableActions = RemoveUselessActions(state);

		Bucket startBucket = state.StartBucket;
		BucketInformation startBucketInfo = state[startBucket];

		Bucket otherBucket = startBucket.GetOtherBucket();
		BucketInformation otherBucketInfo = state[otherBucket];

		switch (startBucket)
		{
			case Bucket.One:
				{
					if (otherBucketInfo.IsFull)
					{
						_ = availableActions.Remove(BucketAction.EmptyBucketOne);
					}

					if (startBucketInfo.IsEmpty)
					{
						_ = availableActions.Remove(BucketAction.FillBucketTwo);
					}

					if (startBucketInfo.HoldingInLiters == otherBucketInfo.UnusedInLiters)
					{
						_ = availableActions.Remove(BucketAction.PourBucketOneIntoBucketTwo);
					}

					break;
				}

			case Bucket.Two:
				{
					if (otherBucketInfo.IsFull)
					{
						_ = availableActions.Remove(BucketAction.EmptyBucketTwo);
					}

					if (startBucketInfo.IsEmpty)
					{
						_ = availableActions.Remove(BucketAction.FillBucketOne);
					}

					if (startBucketInfo.HoldingInLiters == otherBucketInfo.UnusedInLiters)
					{
						_ = availableActions.Remove(BucketAction.PourBucketTwoIntoBucketOne);
					}
				}

				break;

			default:
				break;
		}

		return availableActions;
	}

	private static HashSet<BucketAction> RemoveUselessActions(BucketState state)
	{
		HashSet<BucketAction> availableActions = Enum.GetValues(typeof(BucketAction))
			.Cast<BucketAction>()
			.ToHashSet();
		_ = availableActions.Remove(BucketAction.None);
		_ = availableActions.Remove(BucketAction.QuitImpossibleTask);

		if (state[Bucket.One].IsEmpty)
		{
			_ = availableActions.Remove(BucketAction.EmptyBucketOne);
			_ = availableActions.Remove(BucketAction.PourBucketOneIntoBucketTwo);
		}

		if (state[Bucket.Two].IsFull)
		{
			_ = availableActions.Remove(BucketAction.FillBucketTwo);
			_ = availableActions.Remove(BucketAction.PourBucketOneIntoBucketTwo);
		}

		if (state[Bucket.Two].IsEmpty)
		{
			_ = availableActions.Remove(BucketAction.EmptyBucketTwo);
			_ = availableActions.Remove(BucketAction.PourBucketTwoIntoBucketOne);
		}

		if (state[Bucket.One].IsFull)
		{
			_ = availableActions.Remove(BucketAction.FillBucketOne);
			_ = availableActions.Remove(BucketAction.PourBucketTwoIntoBucketOne);
		}

		return availableActions;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class BucketExtensions
{
	public static Bucket GetOtherBucket(this Bucket bucket)
		=> bucket == Bucket.One
			? Bucket.Two
			: Bucket.One;
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class BucketInformation
{
	public Bucket Identity { get; init; }
	public int CapacityInLiters { get; init; }
	public int HoldingInLiters { get; init; }

	public BucketInformation(Bucket bucket, int capacityInLiters, int holdingInLiters = 0)
	{
		Identity = bucket;
		CapacityInLiters = capacityInLiters;
		HoldingInLiters = holdingInLiters;
	}

	public bool IsEmpty
		=> HoldingInLiters == 0;

	public bool IsFull
		=> HoldingInLiters == CapacityInLiters;

	public int UnusedInLiters
		=> CapacityInLiters - HoldingInLiters;

	public BucketInformation Fill()
		=> new(Identity, CapacityInLiters, CapacityInLiters);

	public BucketInformation Empty()
		=> new(Identity, CapacityInLiters, 0);

	public BucketInformation AddContent(int contentInLiters)
		=> contentInLiters <= 0
			? throw new ArgumentException(
				$"Cannot add {contentInLiters} to bucket {Identity} which is less than or equal to 0 liters"
			)
			: contentInLiters > UnusedInLiters
				? throw new ArgumentException(
					$"Cannot add {contentInLiters} to bucket {Identity} which is more than {UnusedInLiters} liters; already contains {HoldingInLiters} of {CapacityInLiters} liters."
				)
				: new BucketInformation(Identity, CapacityInLiters, HoldingInLiters + contentInLiters);

	public BucketInformation RemoveContent(int contentInLiters)
		=> contentInLiters <= 0
			? throw new ArgumentException(
				$"Cannot remove {contentInLiters} to bucket {Identity} which less than or equal to 0 liters"
			)
			: contentInLiters > HoldingInLiters
				? throw new ArgumentException(
					$"Cannot remove {contentInLiters} to bucket {Identity} which is more than {HoldingInLiters} liters; only contains {HoldingInLiters} of {CapacityInLiters} liters."
				)
				: new BucketInformation(Identity, CapacityInLiters, HoldingInLiters - contentInLiters);

	public bool HasReachedGoal(int? goalInLiters)
		=> HoldingInLiters == goalInLiters;
}

//=======================================================================

// ReSharper disable once CheckNamespace

public class BucketState
{
	private BucketInformation _bucketOne { get; init; }
	private BucketInformation _bucketTwo { get; init; }

	public Bucket StartBucket { get; private init; }

	public BucketState(int bucketOneCapacity, int bucketTwoCapacity, Bucket startBucket)
		: this(
			new(Bucket.One, bucketOneCapacity),
			new(Bucket.Two, bucketTwoCapacity)
		)
		=> StartBucket = startBucket;

	public int Moves { get; private init; }

	private int? _goal { get; init; }

	private BucketState(
		BucketInformation bucketOne,
		BucketInformation bucketTwo
	)
	{
		_bucketOne = bucketOne;
		_bucketTwo = bucketTwo;
		_buckets = new[]
		{
			_bucketOne,
			_bucketTwo,
		};
	}

	private readonly BucketInformation[] _buckets;

	public bool HasReachedGoal
		=> GoalBucket != null;

	public Bucket? GoalBucket
		=> Array.Find(_buckets, x => x.HasReachedGoal(_goal))
			?.Identity;

	public BucketInformation this[Bucket bucket]
		=> _buckets.First(x => x.Identity == bucket);

	public BucketState SetGoal(int goal)
		=> goal <= 0
			? throw new ArgumentException("Goal must be a positive number.")
			: (goal > _bucketOne.CapacityInLiters) &&
			  (goal > _bucketTwo.CapacityInLiters)
				? throw new ArgumentException("Goal larger than both buckets is impossible!")
				: new BucketState(_bucketOne, _bucketTwo) { StartBucket = StartBucket, _goal = goal, Moves = Moves };

	public BucketState Update(BucketInformation[] bucketInformation)
	{
		BucketInformation bucketOne = bucketInformation.FirstOrDefault(
			bucket => bucket.Identity == Bucket.One,
			_bucketOne
		);
		BucketInformation bucketTwo = bucketInformation.FirstOrDefault(
			bucket => bucket.Identity == Bucket.Two,
			_bucketTwo
		);

		ValidateMove(bucketOne, bucketTwo);
		return new(bucketOne, bucketTwo) { StartBucket = StartBucket, _goal = _goal, Moves = Moves + 1 };
	}

	private void ValidateMove(BucketInformation bucketOne, BucketInformation bucketTwo)
	{
		BucketInformation startBucket = StartBucket == bucketOne.Identity
			? bucketOne
			: bucketTwo;

		BucketInformation otherBucket = StartBucket.GetOtherBucket() == bucketOne.Identity
			? bucketOne
			: bucketTwo;

		if (startBucket.IsEmpty &&
			otherBucket.IsFull)
		{
			throw new InvalidBucketStateException(
				"After an action, you may not arrive at a state where the starting bucket is empty and the other bucket is full."
			);
		}
	}

	public BucketState Update(BucketInformation bucketInformation)
		=> Update(
			new[]
			{
				bucketInformation,
			}
		);

	public override int GetHashCode()
		=> _bucketOne.HoldingInLiters ^ _bucketTwo.HoldingInLiters;

	public override bool Equals(object? obj)
		=> obj is BucketState otherState &&
		   (_bucketOne.HoldingInLiters == otherState._bucketOne.HoldingInLiters) &&
		   (_bucketTwo.HoldingInLiters == otherState._bucketTwo.HoldingInLiters);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class Result<T>
{
	private T? _value;

	public T Value
		=> _value != null
			? _value
			: throw new InvalidOperationException("Value is Invalid");

	private Exception? _exception;

	public Exception Exception
		=> _exception ?? throw new InvalidOperationException("Exception is null.");

	public bool IsValid
		=> (_value != null) && (_exception == null);

	public static Result<T> From([DisallowNull] T value)
		=> new() { _value = value };

	public static Result<T> From(Exception exception)
		=> new() { _exception = exception };
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class TwoBucketResult
{
	public required int Moves { get; init; }
	public required Bucket GoalBucket { get; init; }
	public required int OtherBucket { get; init; }

	public static TwoBucketResult From(BucketState state)
	{
		_ = state ?? throw new ArgumentNullException(nameof(state));
		Bucket goalBucket =
			state.GoalBucket ?? throw new ArgumentException("Goal has not been achieved.", nameof(state));
		return new()
		{
			Moves = state.Moves,
			GoalBucket = goalBucket,
			OtherBucket = state[goalBucket.GetOtherBucket()].HoldingInLiters,
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
[Serializable]
public class InvalidBucketStateException : Exception
{
	public InvalidBucketStateException()
	{
	}

	public InvalidBucketStateException(string? message)
		: base(
			message
		)
	{
	}

	public InvalidBucketStateException(string? message, Exception? innerException)
		: base(
			message,
			innerException
		)
	{
	}

	protected InvalidBucketStateException(SerializationInfo info, StreamingContext context)
		: base(
			info,
			context
		)
	{
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
[Serializable]
public class NoSolutionFoundException : Exception
{
	public NoSolutionFoundException()
	{
	}

	public NoSolutionFoundException(string? message)
		: base(
			message
		)
	{
	}

	public NoSolutionFoundException(string? message, Exception? innerException)
		: base(
			message,
			innerException
		)
	{
	}

	protected NoSolutionFoundException(SerializationInfo info, StreamingContext context)
		: base(
			info,
			context
		)
	{
	}

	public static NoSolutionFoundException From(BucketState bucketState)
		=> bucketState is null
			? throw new ArgumentNullException(nameof(bucketState))
			: new(
				$"No solution could be found once with bucket one holding {bucketState[Bucket.One].HoldingInLiters} and bucket one holding {bucketState[Bucket.Two].HoldingInLiters} has been reached."
			);
}

//=======================================================================

// ReSharper disable once CheckNamespace
[Serializable]
public class RepetitionException : Exception
{
	public RepetitionException()
	{
	}

	public RepetitionException(string? message)
		: base(
			message
		)
	{
	}

	public RepetitionException(string? message, Exception? innerException)
		: base(
			message,
			innerException
		)
	{
	}

	protected RepetitionException(SerializationInfo info, StreamingContext context)
		: base(
			info,
			context
		)
	{
	}

	public static RepetitionException From(BucketState bucketState)
		=> bucketState is null
			? throw new ArgumentNullException(nameof(bucketState))
			: new(
				$"State with bucket one holding {bucketState[Bucket.One].HoldingInLiters} and bucket one holding {bucketState[Bucket.Two].HoldingInLiters} has previously been reached."
			);
}

//=======================================================================