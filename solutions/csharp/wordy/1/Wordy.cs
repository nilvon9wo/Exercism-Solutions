using System;
using System.Text.RegularExpressions;
public static partial class Wordy
{
    [GeneratedRegex("\\d+")]
    private static partial Regex NumberRegex();
    
    [GeneratedRegex(@"(?<number1>-?\d+)\s+(?<operation>plus|minus|multiplied by|divided by)\s+(?<number2>-?\d+)(?:\s+(?<leftovers>.+))?")]
    private static partial Regex OperationRegex();
    
    [GeneratedRegex(@"^What is (-?\d+)(?:\?| (plus|minus|multiplied by|divided by) -?\d+)")]
    private static partial Regex ValidQuestionRegex();

    public static int Answer(string question)
    {
        ValidateQuestion(question);
        Match operationMatch = OperationRegex()
            .Match(question);
        return operationMatch.Success 
            ? ProcessOperation(operationMatch) 
            : ExtractNumber(question);
    }

    private static void ValidateQuestion(string question)
    {
        Match validMatch = ValidQuestionRegex()
            .Match(question);
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
        Match match = NumberRegex()
            .Match(text);
        return match.Success 
            ? int.Parse(match.Value) 
            : -1;
    }
}