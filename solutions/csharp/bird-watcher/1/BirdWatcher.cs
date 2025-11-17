using System;
using System.Linq;

internal class BirdCount
{
	private readonly int[] _birdsPerDay;

	public BirdCount(int[] birdsPerDay)
	{
		_birdsPerDay = birdsPerDay;
	}

	private static readonly int[] _lastWeekCounts = new int[] { 0, 2, 5, 3, 7, 8, 4 };

	public static int[] LastWeek()
	{
		return _lastWeekCounts;
	}

	public int Today()
	{
		return _birdsPerDay.Last();
	}

	public void IncrementTodaysCount()
	{
		int lastIndex = _birdsPerDay.Length - 1;
		_birdsPerDay[lastIndex]++;
	}

	public bool HasDayWithoutBirds()
	{
		return _birdsPerDay.Contains(0);
	}

	public int CountForFirstDays(int numberOfDays)
	{
		return _birdsPerDay[..numberOfDays]
			.Sum();
	}

	public int BusyDays()
	{
		return _birdsPerDay
			.Where(x => x >= 5)
			.Count();
	}
}
