using System;

public class CalculationException : Exception
{
	public CalculationException(int operand1, int operand2, string message, Exception inner)
		: base(message, inner)
	{
		if (string.IsNullOrEmpty(message))
		{
			throw new ArgumentException($"'{nameof(message)}' cannot be null or empty.", nameof(message));
		}

		if (inner is null)
		{
			throw new ArgumentNullException(nameof(inner));
		}

		Operand1 = operand1;
		Operand2 = operand2;
	}

	public int Operand1 { get; init; }
	public int Operand2 { get; init; }
}

public class CalculatorTestHarness
{
	private readonly Calculator _calculator;

	public CalculatorTestHarness(Calculator calculator) =>
		_calculator = calculator;

	public string TestMultiplication(int x, int y)
	{
		try
		{
			Multiply(x, y);
			return "Multiply succeeded";
		}
		catch (CalculationException)
		{
			return (x < 0 || y < 0)
				? "Multiply failed for negative operands. Arithmetic operation resulted in an overflow."
				: "Multiply failed for mixed or positive operands. Arithmetic operation resulted in an overflow.";
		}
	}

	public void Multiply(int x, int y)
	{
		try
		{
			_ = _calculator.Multiply(x, y);
		}
		catch (OverflowException exception)
		{
			throw new CalculationException(x, y, "Overflow!", exception);
		}
	}
}

// Please do not modify the code below.
// If there is an overflow in the multiplication operation
// then a System.OverflowException is thrown.
public class Calculator
{
	public int Multiply(int x, int y)
	{
		checked
		{
			return x * y;
		}
	}
}
