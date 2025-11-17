using System.Globalization;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class RunLengthEncoding
{
	private static readonly CultureInfo _invariantCulture = CultureInfo.InvariantCulture;

	public static string Encode(string input)
	{
		if (string.IsNullOrEmpty(input))
		{
			return input;
		}

		EncodeState initialSegment = new() { Character = input[0], Count = 1, Result = "" };
		string encoded = input.Skip(1)
			.Aggregate(initialSegment, EncodeCharacter)
			.Result;
		string countString = initialSegment.Count > 1
			? initialSegment.Count.ToString(_invariantCulture)
			: "";
		return $"{encoded}{countString}{initialSegment.Character}";
	}

	private static EncodeState EncodeCharacter(EncodeState segment, char c)
	{
		if (c == segment.Character)
		{
			segment.Count++;
		}
		else
		{
			segment.Result += segment.Count > 1
				? segment.Count.ToString(_invariantCulture)
				: "";
			segment.Result += segment.Character;
			segment.Character = c;
			segment.Count = 1;
		}

		return segment;
	}

	public static string Decode(string input)
		=> string.IsNullOrEmpty(input)
			? input
			: input.Aggregate(
					new DecodeState { Result = "" },
					(acc, c) => char.IsDigit(c)
						? new()
						{
							Result = acc.Result,
							Count = (acc.Count * 10) + (c - '0'),
							Character = acc.Character,
						}
						: new DecodeState
						{
							Result = acc.Result
									 + (acc.Count == 0
										 ? c.ToString()
										 : new(c, acc.Count)),
							Count = 0,
							Character = c,
						}
				)
				.Result;
}

// ReSharper disable once CheckNamespace
internal class EncodeState
{
	public required string Result { get; set; }
	public int Count { get; set; }
	public char Character { get; set; }
}

// ReSharper disable once CheckNamespace
internal class DecodeState
{
	public required string Result { get; init; }
	public int Count { get; init; }
	public char Character { get; init; }
}