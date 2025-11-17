using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

public class Forth
{
	private static readonly Regex NewDefinitionPattern = new("^:(.*);$", RegexOptions.Compiled);

	private static readonly Dictionary<string, Func<Context, Context>> _defaultHandlerByToken
		= new(StringComparer.OrdinalIgnoreCase){
			{ "+", MathHelpers.Add },
			{ "-", MathHelpers.Subtract },
			{ "*", MathHelpers.Multiply },
			{ "/", MathHelpers.Divide },
			{ "dup", StackHelpers.DuplicateLast },
			{ "drop", StackHelpers.DropLast },
			{ "swap", StackHelpers.SwapLast },
			{ "over", StackHelpers.PenultimateValueCopy },
		};

	public static string Evaluate(string[] instructions) =>
		instructions.Aggregate(new Context(), Evaluate)
			.ToString();

	private static Context Evaluate(Context context, string instruction)
	{
		Match instructionMatch = NewDefinitionPattern.Match(instruction);
		return instructionMatch.Success
			? context.DefineHandler(instructionMatch.Groups[1])
			: FollowInstruction(context, instruction);
	}

	private static Context FollowInstruction(Context context, string instruction)
	{
		List<string> tokens = instruction.Split(' ')
					.ToList();

		while (tokens.Any())
		{
			string nextOperation = ShiftTokensToStack(context, tokens);
			context = Do(context, nextOperation);
		}

		return context;
	}

	private static string ShiftTokensToStack(Context context, List<string> tokens)
	{
		string nextOperation = null;
		do
		{
			string token = tokens.Shift();
			if (int.TryParse(token, out int value))
			{
				context.Stack.Add(value);
			}
			else
			{
				nextOperation = token;
			}
		}
		while (tokens.Any() && string.IsNullOrEmpty(nextOperation));
		return nextOperation;
	}

	private static Context Do(Context context, string operation) =>
		string.IsNullOrEmpty(operation)
			? context
			: context.InstructionsByWord.TryGetValue(operation, out string instruction)
				? FollowInstruction(context, instruction)
				: _defaultHandlerByToken.TryGetValue(operation, out Func<Context, Context> function)
					? function(context)
					: throw new InvalidOperationException($"Unknown operation: `{operation}`.");
}

public class Context
{
	public Dictionary<string, string> InstructionsByWord { get; init; } = new(StringComparer.OrdinalIgnoreCase);

	public List<int> Stack { get; init; } = new();

	public override string ToString() =>
		string.Join(' ', Stack);

	public Context DefineHandler(Group group)
	{
		List<string> definition = group.Value.Split(' ')
			.Where(x => !string.IsNullOrEmpty(x))
			.ToList()
			.RequireAtLeastTwoValues();

		string word = definition.Shift();
		if (int.TryParse(word, out int _))
		{
			throw new InvalidOperationException("Redefining numbers not allowed.");
		}

		InstructionsByWord[word] = CreateInstruction(definition);
		return this;
	}

	private string CreateInstruction(List<string> definition)
	{
		StringBuilder builder = new();
		foreach (string word in definition)
		{
			_ = InstructionsByWord.TryGetValue(word, out string instruction)
				? builder.Append($"{instruction} ")
				: builder.Append($"{word} ");
		}

		return builder.ToString()
			.Trim();
	}

	public Context WithStack(Action<List<int>> function)
	{
		function(Stack);
		return this;
	}
}

public class MathHelpers
{
	public static Context Add(Context context) =>
		DoMath<int>(context, (x, y) => x + y);

	public static Context Subtract(Context context) =>
		DoMath<int>(context, (x, y) => x - y);

	public static Context Multiply(Context context) =>
		DoMath<int>(context, (x, y) => x * y);

	public static Context Divide(Context context) =>
		DoMath<int>(context, (x, y) => x / y);

	private static Context DoMath<T>(Context context, Func<int, int, int> function) =>
		context.WithStack(stack =>
			{
				int x = stack.RequireExactlyTwoValues()
					.Shift();
				int y = stack.Shift();
				int result = function(x, y);
				stack.Add(result);
			});
}

public class StackHelpers
{
	public static Context DuplicateLast(Context context) =>
		context.WithStack(stack =>
		{
			int last = stack.RequireAtLeastOneValue()
				.LastOrDefault();
			stack.Add(last);
		});

	public static Context DropLast(Context context) =>
		context.WithStack(stack =>
			_ = stack.RequireAtLeastOneValue()
				.Pop()
		);

	public static Context SwapLast(Context context) =>
		context.WithStack(stack =>
		{
			int lastValue = stack.RequireAtLeastTwoValues()
				.Pop();
			int penultimateValue = stack.Pop();
			stack.Add(lastValue);
			stack.Add(penultimateValue);
		});

	public static Context PenultimateValueCopy(Context context) =>
		context.WithStack(stack =>
		{
			int penultimateValue = stack.RequireAtLeastTwoValues()[^2];
			stack.Add(penultimateValue);
		});
}

public static class ListExtensions
{
	public static List<T> RequireAtLeastOneValue<T>(this List<T> values) =>
		values.RequiresCount(count => count < 1, "Operation requires at least one value.");

	public static List<T> RequireAtLeastTwoValues<T>(this List<T> values) =>
		values.RequiresCount(count => count < 2, "Operation requires at least two values.");

	public static List<T> RequireExactlyTwoValues<T>(this List<T> values) =>
		values.RequiresCount(count => count != 2, "Operation requires exactly two values.");

	private static List<T> RequiresCount<T>(this List<T> values, Func<int, bool> validation, string errorMessage) =>
		validation(values.Count)
			? throw new InvalidOperationException(errorMessage)
			: values;

	public static T Shift<T>(this List<T> values) =>
		TakeOne(values, x => x.FirstOrDefault());

	public static T Pop<T>(this List<T> values) =>
		TakeOne(values, x => x.LastOrDefault());

	private static T TakeOne<T>(this List<T> values, Func<List<T>, T> function)
	{
		T item = function(values);
		if (item != null)
		{
			_ = values.Remove(item);
		}

		return item;
	}
}
