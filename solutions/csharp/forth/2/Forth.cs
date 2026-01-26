using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

// ReSharper disable once CheckNamespace
public static class Forth
{
    private static readonly Regex _newDefinitionPattern = new("^:(.*);$", RegexOptions.Compiled);

    private static readonly Dictionary<string, Func<Context, Context>> _defaultHandlerByToken
        = new(StringComparer.OrdinalIgnoreCase)
        {
            { "+", MathHelpers.Add },
            { "-", MathHelpers.Subtract },
            { "*", MathHelpers.Multiply },
            { "/", MathHelpers.Divide },
            { "dup", StackHelpers.DuplicateLast },
            { "drop", StackHelpers.DropLast },
            { "swap", StackHelpers.SwapLast },
            { "over", StackHelpers.PenultimateValueCopy },
        };

    public static string Evaluate(string[] instructions)
        => instructions.Aggregate(new Context(), Evaluate)
            .ToString();

    private static Context Evaluate(Context context, string instruction)
    {
        Match instructionMatch = _newDefinitionPattern.Match(instruction);
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
        } while (tokens.Any()
                 && string.IsNullOrEmpty(nextOperation));

        return nextOperation;
    }

    private static Context Do(Context context, string operation)
        => string.IsNullOrEmpty(operation)
            ? context
            : context.InstructionsByWord.TryGetValue(operation, out string instruction)
                ? FollowInstruction(context, instruction)
                : _defaultHandlerByToken.TryGetValue(operation, out Func<Context, Context> function)
                    ? function(context)
                    : throw new InvalidOperationException($"Unknown operation: `{operation}`.");
}
