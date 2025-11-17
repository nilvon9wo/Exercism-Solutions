using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Text.RegularExpressions;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not compatible with tests.")]
[SuppressMessage("Roslynator", "RCS1110:Declare type inside namespace.", Justification = "Not compatible with tests.")]
[SuppressMessage("GeneratedRegex", "SYSLIB1045:Convert to 'GeneratedRegexAttribute'.", Justification = "Causes tests to fail on Exercism.")]
public static class Wordy
{
    private static readonly Regex NumberRegex = new("\\d+");
    private static readonly Regex OperationRegex = new(@"(?<number1>-?\d+)\s+(?<operation>[\p{L}\s]+)\s+(?<number2>-?\d+)(?:\s+(?<leftovers>.+))?");
    private static readonly Regex ValidQuestionRegex = new(@"^What is (-?\d+)(?:\?| .* -?\d+)");

    private static readonly Dictionary<string, Func<int, int, int>> Operations = new()
    {
        { "plus", (a, b)
            => a + b
        },

        { "minus", (a, b)
             => a - b
        },

        { "multiplied by", (a, b)
            => a * b
        },

        { "divided by", (a, b)
            => (b != 0)
                    ? a / b
                    : throw new ArgumentException("Division by zero is not allowed.")
        },
    };

    public static int Answer(string question)
    {
        ValidateQuestion(question);
        Match operationMatch = OperationRegex.Match(question);
        return operationMatch.Success
            ? ProcessOperation(operationMatch)
            : ExtractNumber(question);
    }

    private static void ValidateQuestion(string question)
    {
        Match validMatch = ValidQuestionRegex.Match(question);
        if (!validMatch.Success)
        {
            throw new ArgumentException("Invalid question: " + question);
        }
    }

    private static int ProcessOperation(Match operationMatch)
    {
        int result = Calculate(operationMatch);
        string leftovers = operationMatch.Groups["leftovers"].Value;
        return (!string.IsNullOrWhiteSpace(leftovers))
            ? Answer($"What is {result} {leftovers}")
            : result;
    }

    private static int Calculate(Match operationMatch)
    {
        int number1 = int.Parse(operationMatch.Groups["number1"].Value);
        int number2 = int.Parse(operationMatch.Groups["number2"].Value);
        string operation = operationMatch.Groups["operation"].Value.ToLower();
        int result = Operations.TryGetValue(operation, out Func<int, int, int> function)
            ? function(number1, number2)
            : throw new ArgumentException("Unsupported operation: " + operation);
        return result;
    }

    private static int ExtractNumber(string text)
    {
        Match match = NumberRegex.Match(text);
        return match.Success
            ? int.Parse(match.Value)
            : -1;
    }
}