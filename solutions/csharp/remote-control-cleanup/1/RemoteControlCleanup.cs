using System;
using System.Collections.Generic;

public class RemoteControlCar
{
	public Telemetry Telemetry { get; private set; } = new();

	public string CurrentSponsor =>
		Telemetry.Sponsor;

	public string GetSpeed() =>
		Telemetry.Speed.ToString();
}

public class Telemetry
{
	public string Sponsor { get; set; }
	public Speed Speed { get; set; }

	public void Calibrate()
	{
	}

	public bool SelfTest() =>
		true;

	public void ShowSponsor(string sponsor) =>
		Sponsor = sponsor;

	public void SetSpeed(decimal speed, string units) =>
		Speed = new(speed, units.ToSpeedUnits());
}

public readonly struct Speed
{
	public decimal Amount { get; }
	public SpeedUnits SpeedUnits { get; }

	public Speed(decimal amount, SpeedUnits speedUnits)
	{
		Amount = amount;
		SpeedUnits = speedUnits;
	}

	public override string ToString() =>
		$"{Amount} {SpeedUnits.ToVerbose()}";
}

public enum SpeedUnits
{
	[Abbreviation("mps")]
	[Verbose("meters per second")]
	MetersPerSecond,

	[Abbreviation("cps")]
	[Verbose("centimeters per second")]
	CentimetersPerSecond
}

public static class SpeedUnitsExtensions
{
	private static Dictionary<string, SpeedUnits> _speedUnitsByString;
	public static Dictionary<string, SpeedUnits> SpeedUnitByString
	{
		get
		{
			if (_speedUnitsByString == null)
			{
				_speedUnitsByString = new();
				foreach (SpeedUnits level in Enum.GetValues(typeof(SpeedUnits)))
				{
					string levelString = ToAbbreviation(level);
					_speedUnitsByString[levelString] = level;
				}
			}

			return _speedUnitsByString;
		}
	}

	public static string ToAbbreviation(this SpeedUnits value) =>
		ToText<AbbreviationAttribute>(value);

	public static string ToVerbose(this SpeedUnits value) =>
		ToText<VerboseAttribute>(value);

	private static string ToText<T>(this SpeedUnits value)
		where T : Attribute, INamed
	{
		T[] attributes = (T[])value
		   .GetType()
		   .GetField(value.ToString())
		   .GetCustomAttributes(typeof(T), false);

		return attributes.Length > 0
			? attributes[0].Name
			: string.Empty;
	}

	public static SpeedUnits ToSpeedUnits(this string value) =>
		SpeedUnitByString.TryGetValue(value, out SpeedUnits level)
			? level
			: SpeedUnits.MetersPerSecond;
}

public class AbbreviationAttribute : TextAttribute
{
	public AbbreviationAttribute(string name) : base(name) { }

	public new string Name => throw new NotImplementedException();
}

public class VerboseAttribute : TextAttribute
{
	public VerboseAttribute(string name) : base(name) { }
}

[AttributeUsage(AttributeTargets.Field)]
public abstract class TextAttribute : Attribute, INamed
{
	public readonly string Name;
	public double version;

	public TextAttribute(string name)
	{
		Name = name;
		version = 1.0;
	}

	string INamed.Name =>
		Name;
}

public interface INamed
{
	string Name { get; }
}
