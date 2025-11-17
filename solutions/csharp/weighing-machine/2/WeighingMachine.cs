using System;

public class WeighingMachine
{
	public WeighingMachine(int precision) =>
		Precision = precision;

	public int Precision { get; }

	private double _weight;
	public double Weight
	{
		get => _weight;
		set
		{
			if (value < 0)
			{
				throw new ArgumentOutOfRangeException(nameof(value));
			}

			_weight = value;
		}
	}

	public double TareAdjustment { get; set; } = 5;

	public string DisplayWeight =>
		$"{Weight - TareAdjustment:#,0.000} kg";
}
