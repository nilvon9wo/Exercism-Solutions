using System;

public static class RealNumberExtension
{
	public static double Expreal(this int realNumber, RationalNumber exponent) =>
		exponent.Expreal(realNumber);
}

public struct RationalNumber
{
	public readonly int Numerator { get; init; }
	public readonly int Denominator { get; init; }
	public RationalNumber(int numerator, int denominator)
	{
		Numerator = numerator;
		Denominator = denominator;
	}

	public static RationalNumber operator +(RationalNumber augend, RationalNumber addend)
	{
		int leastCommonDenominator = LeastCommonMultiplier(augend.Denominator, addend.Denominator);
		int numerator1 = augend.Numerator * leastCommonDenominator / augend.Denominator;
		int numerator2 = addend.Numerator * leastCommonDenominator / addend.Denominator;
		int sumNumerator = numerator1 + numerator2;
		return new RationalNumber(sumNumerator, leastCommonDenominator)
			.Reduce();
	}

	private static int LeastCommonMultiplier(int a, int b) =>
		a * b / GreatestCommonDenominator(a, b);

	private static int GreatestCommonDenominator(int a, int b) =>
		(a == 0)
			? b
			: GreatestCommonDenominator(b % a, a);

	public static RationalNumber operator -(RationalNumber minuend, RationalNumber subtrahend)
	{
		RationalNumber augend = minuend;
		RationalNumber addend = new(-subtrahend.Numerator, subtrahend.Denominator);
		return augend + addend;
	}

	public static RationalNumber operator *(RationalNumber multiplier, RationalNumber multiplicand)
	{
		int productNumerator = multiplier.Numerator * multiplicand.Numerator;
		int productDenominator = multiplier.Denominator * multiplicand.Denominator;
		return new RationalNumber(productNumerator, productDenominator)
			.Reduce();
	}

	public static RationalNumber operator /(RationalNumber dividend, RationalNumber divisor)
	{
		RationalNumber productNumerator = dividend;
		RationalNumber productDenominator = new(divisor.Denominator, divisor.Numerator);
		return productNumerator * productDenominator;
	}

	public RationalNumber Abs() =>
		new(Math.Abs(Numerator), Math.Abs(Denominator));

	public RationalNumber Reduce()
	{
		if (Numerator == 0)
		{
			return new(0, 1);
		}

		if (Numerator == Denominator)
		{
			return new RationalNumber(1, 1);
		}

		if (Denominator < 0)
		{
			return new RationalNumber(-Numerator, -Denominator)
				.Reduce();
		}

		int abstractNumerator = Math.Abs(Numerator);
		int abstractDenominator = Math.Abs(Denominator);
		int temp = abstractNumerator < abstractDenominator
			? abstractDenominator
			: abstractNumerator;

		for (int i = temp / 2; i > 1; i--)
		{
			if (Numerator % i == 0 && Denominator % 1 == 0)
			{
				return new RationalNumber(Numerator / i, Denominator / i)
					.Reduce();
			}
		}

		return this;
	}

	public RationalNumber Exprational(int power)
	{
		double doublePower = Convert.ToDouble(power);
		double doubleNumerator = Convert.ToDouble(Numerator);
		double doubleDenominator = Convert.ToDouble(Denominator);
		double powerNumerator = Math.Pow(doubleNumerator, doublePower);
		double powerDenominator = Math.Pow(doubleDenominator, doublePower);
		return new(Convert.ToInt32(powerNumerator), Convert.ToInt32(powerDenominator));
	}

	public double Expreal(int baseNumber)
	{
		double power = Numerator / (double)Denominator;
		return Math.Pow(baseNumber, power);
	}

	public override string ToString() =>
		$"{Numerator}/{Denominator}";
}