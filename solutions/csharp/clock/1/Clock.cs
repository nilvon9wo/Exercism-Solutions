using System;

public class Clock
{
	private readonly TimeOnly _timeOnly;

	public Clock(int hours, int minutes) : this(new TimeOnly()
			.AddHours(hours), minutes)
	{
	}

	private Clock(TimeOnly timeOnly, int minutes) =>
		_timeOnly = timeOnly.AddMinutes(minutes);

	public Clock Add(int minutesToAdd) =>
		new(_timeOnly, minutesToAdd);

	public Clock Subtract(int minutesToSubtract) =>
		Add(-minutesToSubtract);

	public override string ToString() =>
		_timeOnly.ToString("r")[..5];

	public override bool Equals(object other) =>
		other != null
		&& other is Clock that
		&& that.ToString() == ToString();

	public override int GetHashCode() =>
		_timeOnly.GetHashCode();
}
