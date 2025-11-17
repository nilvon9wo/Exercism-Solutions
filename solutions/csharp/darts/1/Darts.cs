using System;

public static class Darts
{
	private const int _outsideTargetPoints = 0;
	private const int _outerCirclePoints = 1;
	private const int _middlerCirclePoints = 5;
	private const int _innerCirclePoints = 10;

	private const int _outerCircleRadius = 10;
	private const int _middleCircleRadius = 5;
	private const int _innerCircleRadius = 1;

	public static int Score(double x, double y) =>
		Pythagoras.GetDistanceFromCenter(x, y) switch
		{
			<= _innerCircleRadius => _innerCirclePoints,
			<= _middleCircleRadius => _middlerCirclePoints,
			<= _outerCircleRadius => _outerCirclePoints,
			_ => _outsideTargetPoints
		};
}

public static class Pythagoras
{
	public static double GetDistanceFromCenter(double x, double y) =>
		Math.Sqrt(x.Squared() + y.Squared());

	private static double Squared(this double x) =>
		Math.Pow(x, 2);
}