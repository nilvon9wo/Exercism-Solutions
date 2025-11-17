using System;

public static class SimpleCalculator
{
	public static string Calculate(int operand1, int operand2, string operation)
	{
		switch (operation)
		{
			case null:
				throw new ArgumentNullException(nameof(operation));
			case "":
				throw new ArgumentException(nameof(operation));
			case "/" when operand2 == 0:
				return "Division by zero is not allowed.";
		}

		int result = operation switch
		{
			"+" => operand1 + operand2,
			"*" => operand1 * operand2,
			"/" => operand1 / operand2,
			"-" => operand1 - operand2,
			_ => throw new ArgumentOutOfRangeException(nameof(operation)),
		};
		return $"{operand1} {operation} {operand2} = {result}";
	}
}
