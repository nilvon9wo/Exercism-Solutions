internal class RemoteControlCar
{
	private readonly int _speed;
	private readonly int _batteryDrain;

	public RemoteControlCar(int speed, int batteryDrain)
	{
		_speed = speed;
		_batteryDrain = batteryDrain;
	}

	private int _distanceDriven = 0;
	private int _batteryLife = 100;

	public bool BatteryDrained() =>
		_batteryLife < _batteryDrain;

	public int DistanceDriven() =>
		_distanceDriven;

	public void Drive()
	{
		if (!BatteryDrained())
		{
			_distanceDriven += _speed;
			_batteryLife -= _batteryDrain;
		}
	}

	public static RemoteControlCar Nitro() =>
		new(50, 4);
}

internal class RaceTrack
{
	private readonly int _distance;

	public RaceTrack(int distance) => _distance = distance;

	public bool TryFinishTrack(RemoteControlCar car)
	{
		int distanceDriven = 0;
		while (!car.BatteryDrained() && !IsComplete(distanceDriven))
		{
			car.Drive();
			distanceDriven = car.DistanceDriven();
		}

		return IsComplete(distanceDriven);
	}

	private bool IsComplete(int distanceDriven) =>
		distanceDriven >= _distance;
}
