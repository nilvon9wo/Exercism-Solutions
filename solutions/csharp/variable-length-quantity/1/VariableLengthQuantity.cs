using System;
using System.Collections.Generic;
using System.Linq;

public static class VariableLengthQuantity
{
	public static uint[] Encode(uint[] numbers) =>
		numbers.SelectMany(EncodeNumber)
			.ToArray();

	private static IEnumerable<uint> EncodeNumber(uint number)
	{
		List<uint> result = new()
		{
			number.ToFirst7Bits()
		};

		number >>= 7;
		while (number != 0)
		{
			result.Add(number.ToFirst7Bits() | Masks.ContinuationMask);
			number >>= 7;
		}

		return Enumerable.Reverse(result);
	}

	public static uint[] Decode(uint[] bytes) =>
		DecodeFragments(bytes).ToArray();

	private static IEnumerable<uint> DecodeFragments(uint[] fragments)
	{
		if (fragments.Last().HasContinuationBit())
		{
			throw new InvalidOperationException("Input is not a complete VLQ-encoded sequence");
		}

		uint result = 0U;
		foreach (uint fragment in fragments)
		{
			result = (result << 7) + fragment.ToFirst7Bits();
			if (!fragment.HasContinuationBit())
			{
				yield return result;
				result = 0;
			}
		}
	}
}

public class Masks
{
	public const uint ContinuationMask = 0x80u;
}

public static class UIntExtensions
{
	public static uint ToFirst7Bits(this uint number) =>
		number & 0x7fu;

	public static bool HasContinuationBit(this uint number) =>
		(number & Masks.ContinuationMask) != 0;
}