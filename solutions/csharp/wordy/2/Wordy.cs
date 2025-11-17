using System;
using System.Diagnostics.CodeAnalysis;
using System.Text.RegularExpressions;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "Not compatible with tests.")]
[SuppressMessage("Roslynator", "RCS1110:Declare type inside namespace.", Justification = "Not compatible with tests.")]
[SuppressMessage("GeneratedRegex", "SYSLIB1045:Convert to 'GeneratedRegexAttribute'.", Justification = "Causes tests to fail on Exercism.")]
public static class Wordy
{
    private static readonly Regex NumberRegex = new("\\d+");
    private static readonly Regex OperationRegex = new(@"(?<number1>-?\d+)\s+(?<operation>plus|minus|multiplied by|divided by)\s+(?<number2>-?\d+)(?:\s+(?<leftovers>.+))?");
    private static readonly Regex ValidQuestionRegex = new(@"^What is (-?\d+)(?:\?| (plus|minus|multiplied by|divided by) -?\d+)");

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
        int number1 = int.Parse(operationMatch.Groups["number1"].Value);
        int number2 = int.Parse(operationMatch.Groups["number2"].Value);
        string operation = operationMatch.Groups["operation"].Value.ToLower();
        string leftovers = operationMatch.Groups["leftovers"].Value;

        int result = operation switch
        {
            "plus" =>
                number1 + number2,

            "minus" =>
                number1 - number2,

            "multiplied by" =>
                number1 * number2,

            "divided by" =>
                (number2 != 0)
                    ? number1 / number2
                    : throw new ArgumentException("Division by zero is not allowed."),

            _ => throw new ArgumentException("Unsupported operation: " + operation)
        };

        return (!string.IsNullOrWhiteSpace(leftovers))
            ? Answer($"What is {result} {leftovers}")
            : result;
    }

    private static int ExtractNumber(string text)
    {
        Match match = NumberRegex.Match(text);
        return match.Success
            ? int.Parse(match.Value)
            : -1;
    }
}