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

		RunLengthSegment initialSegment = new() { Character = input[0], Count = 1, Encoded = "" };
		string encoded = input.Skip(1)
			.Aggregate(initialSegment, EncodeCharacter)
			.Encoded;
		string countString = initialSegment.Count > 1
			? initialSegment.Count.ToString(_invariantCulture)
			: "";
		return $"{encoded}{countString}{initialSegment.Character}";
	}

	private static RunLengthSegment EncodeCharacter(RunLengthSegment segment, char c)
	{
		if (c == segment.Character)
		{
			segment.Count++;
		}
		else
		{
			segment.Encoded += segment.Count > 1
				? segment.Count.ToString(_invariantCulture)
				: "";
			segment.Encoded += segment.Character;
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
							PreviousCharacter = acc.PreviousCharacter,
						}
						: new DecodeState
						{
							Result = acc.Result
									 + (acc.Count == 0
										 ? c.ToString()
										 : new(c, acc.Count)),
							Count = 0,
							PreviousCharacter = c,
						}
				)
				.Result;
}

// ReSharper disable once CheckNamespace
internal class RunLengthSegment
{
	public int Count { get; set; }
	public char Character { get; set; }
	public required string Encoded { get; set; }
}

// ReSharper disable once CheckNamespace
internal class DecodeState
{
	public required string Result { get; init; }
	public int Count { get; init; }
	public char PreviousCharacter { get; init; }
}