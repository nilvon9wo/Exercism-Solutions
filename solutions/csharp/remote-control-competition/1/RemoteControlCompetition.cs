using System;
using System.Collections.Generic;

public interface IRemoteControlCar
{
	int DistanceTravelled { get; }
	void Drive();
}

public class ProductionRemoteControlCar
	: IRemoteControlCar, IComparable<ProductionRemoteControlCar>, IEquatable<ProductionRemoteControlCar>
{
	public int DistanceTravelled { get; private set; }
	public int NumberOfVictories { get; set; }

	public int CompareTo(ProductionRemoteControlCar other) =>
		(NumberOfVictories < other.NumberOfVictories)
			? -1
			: 1;

	public void Drive() =>
		DistanceTravelled += 10;

	public bool Equals(ProductionRemoteControlCar other) =>
		other != null
			&& other.DistanceTravelled == DistanceTravelled
			&& other.NumberOfVictories == NumberOfVictories;
}

public class ExperimentalRemoteControlCar : IRemoteControlCar
{
	public int DistanceTravelled { get; private set; }

	public void Drive() =>
		DistanceTravelled += 20;
}

public static class TestTrack
{
	public static void Race(IRemoteControlCar car) =>
		car.Drive();

	public static List<ProductionRemoteControlCar> GetRankedCars(ProductionRemoteControlCar car1,
		ProductionRemoteControlCar car2)
	{
		List<ProductionRemoteControlCar> cars = new() { car1, car2 };
		cars.Sort();
		return cars;
	}
}
