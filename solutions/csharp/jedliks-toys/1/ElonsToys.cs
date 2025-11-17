internal class RemoteControlCar
{
	private int _distance = 0;
	private const int _driveIncrement = 20;
	private int _battery = 100;
	private const int _batteryDecrement = 1;

	public static RemoteControlCar Buy() =>
		new();

	public string DistanceDisplay() =>
		$"Driven {_distance} meters";

	public string BatteryDisplay() =>
		HasBatteryEnergy()
			? $"Battery at {_battery}%"
			: "Battery empty";

	public void Drive()
	{
		if (HasBatteryEnergy())
		{
			_distance += _driveIncrement;
			_battery -= _batteryDecrement;
		}
	}

	private bool HasBatteryEnergy() =>
		_battery > 0;
}
