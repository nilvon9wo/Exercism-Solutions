using System;

internal static class AssemblyLine
{
	private const int _offSpeed = 0;
	private const int _maxSpeed = 10;
	private const int _carsPerHourPerLevel = 221;
	private const int _minutesPerHour = 60;

	public static double SuccessRate(int speed) =>
		speed switch
		{
			_offSpeed => 0,
			<= 4 => 1,
			<= 8 => 0.9,
			<= 9 => 0.8,
			<= _maxSpeed => 0.77,
			_ => throw new NotImplementedException($"Maximum speed is {_maxSpeed}."),
		};

	public static double ProductionRatePerHour(int speed) =>
		speed * _carsPerHourPerLevel * SuccessRate(speed);

	public static int WorkingItemsPerMinute(int speed) =>
		(int)(ProductionRatePerHour(speed) / _minutesPerHour);
}
