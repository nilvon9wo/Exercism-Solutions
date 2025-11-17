using System;

public readonly struct ComplexNumber
{
	private readonly double _real;
	private readonly double _imaginary;
	public ComplexNumber(double real, double imaginary)
	{
		_real = real;
		_imaginary = imaginary;
	}

	public double Real() =>
		_real;

	public double Imaginary() =>
		_imaginary;

	public ComplexNumber Mul(ComplexNumber other)
	{
		double realPart = (_real * other._real) - (_imaginary * other._imaginary);
		double imaginaryPart = (_real * other._imaginary) + (_imaginary * other._real);
		return new ComplexNumber(realPart, imaginaryPart);
	}

	public ComplexNumber Add(ComplexNumber other)
	{
		double realPart = _real + other._real;
		double imaginaryPart = _imaginary + other._imaginary;
		return new ComplexNumber(realPart, imaginaryPart);
	}

	public ComplexNumber Sub(ComplexNumber other)
	{
		double realPart = _real - other._real;
		double imaginaryPart = _imaginary - other._imaginary;
		return new ComplexNumber(realPart, imaginaryPart);
	}

	public ComplexNumber Div(ComplexNumber other)
	{
		double numerator1 = (_real * other._real) + (_imaginary * other._imaginary);
		double denominator = other._imaginary.Squared() + other._real.Squared();
		double realPart = numerator1 / denominator;

		double numerator2 = (_imaginary * other._real) - (_real * other._imaginary);
		double imaginaryPart = numerator2 / denominator;
		return new ComplexNumber(realPart, imaginaryPart);
	}

	public double Abs() =>
		Math.Sqrt(_real.Squared() + _imaginary.Squared());

	public ComplexNumber Conjugate() =>
		new(_real, -_imaginary);

	public ComplexNumber Exp()
	{
		ComplexNumber a = new(Math.Exp(_real), 0);
		ComplexNumber b = new(Math.Cos(_imaginary), 0);
		ComplexNumber c = new(0, Math.Sin(_imaginary));
		return a.Mul(b.Add(c));
	}

	public static implicit operator ComplexNumber(int integer) =>
		new(integer, 0);
}

public static class DoubleExtensions
{
	public static double Squared(this double value)
		=> Math.Pow(value, 2);
}