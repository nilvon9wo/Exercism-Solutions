using System;
using System.Collections.Generic;
using System.Linq;

public static class SecretHandshake
{
	public static string[] Commands(int commandValue)
	{
		Signal command = (Signal)commandValue;
		List<Signal> signals = command.GetSignals()
			.ToList();

		if (signals.Contains(Signal.Reverse))
		{
			_ = signals.Remove(Signal.Reverse);
			signals.Reverse();
		}

		return signals.Select(x => x.ToValue())
			.ToArray();
	}

	private static IEnumerable<Signal> GetSignals(this Signal values)
	{
		foreach (Signal signal in Enum.GetValues(typeof(Signal)))
		{
			if (values.HasFlag(signal))
			{
				yield return signal;
			}
		}
	}

	private static string ToValue(this Signal signal)
	{
		SignalTextAttribute[] attributes = GetAttributes(signal);
		return attributes.Length > 0
			? attributes[0].Value.ToString()
			: "";
	}

	private static SignalTextAttribute[] GetAttributes(this Signal signal)
	{
		SignalTextAttribute[] attributes = (SignalTextAttribute[])signal
		   .GetType()
		   .GetField(signal.ToString())
		   .GetCustomAttributes(typeof(SignalTextAttribute), false);

		return attributes;
	}
}

[Flags]
public enum Signal
{
	[SignalText("wink")]
	Wink = 1,

	[SignalText("double blink")]
	DoubleBlink = 2,

	[SignalText("close your eyes")]
	CloseYourEyes = 4,

	[SignalText("jump")]
	Jump = 8,

	Reverse = 16,
}

internal class SignalTextAttribute : ValueAttribute
{
	public SignalTextAttribute(string value) : base(value)
	{
	}
}

internal abstract class ValueAttribute : Attribute
{
	public ValueAttribute(string value) =>
	Value = value;

	public string Value { get; }
}