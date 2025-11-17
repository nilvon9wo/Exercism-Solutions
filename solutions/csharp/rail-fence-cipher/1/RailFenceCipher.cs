using System;
using System.Linq;

// ReSharper disable once CheckNamespace
public class RailFenceCipher
{
	private readonly int _rails;

	public RailFenceCipher(int rails)
		=> _rails = rails;

	public string Encode(string input)
	{
		if (string.IsNullOrWhiteSpace(input))
		{
			throw new ArgumentException($"'{nameof(input)}' cannot be null or whitespace.", nameof(input));
		}

		EncodeState state = new();
		return input.ToCharArray()
			.Aggregate(
				new string[_rails],
				(strings, character) =>
				{
					strings[state.TrackIndex] += character;
					state.TrackIndex += state.Direction;
					state.DirectionCount++;

					if (state.DirectionCount == (_rails - 1))
					{
						state.Direction *= -1;
						state.DirectionCount = 0;
					}

					return strings;
				}
			)
			.Aggregate("", (x, y) => x + y);
	}

	public string Decode(string input)
	{
		if (string.IsNullOrWhiteSpace(input))
		{
			return input;
		}

		DecodePositionManager state = new(input.Length, _rails);
		int[] indexes = Enumerable.Range(0, input.Length)
			.OrderBy(_ => state.GetNextPosition())
			.ToArray();

		return input.Select((character, index) => new DecodePair(character, indexes[index]))
			.OrderBy(decodePair => decodePair.Index)
			.Aggregate("", (accumulator, decodePair) => accumulator + decodePair.Character);
	}
}

// ReSharper disable once CheckNamespace
internal class EncodeState
{
	internal int TrackIndex { get; set; }
	internal int Direction { get; set; } = 1;
	internal int DirectionCount { get; set; }
}

// ReSharper disable once CheckNamespace
internal class DecodePositionManager
{
	private readonly int _messageLength;
	private readonly int _rails;

	internal DecodePositionManager(int messageLength, int rails)
	{
		_messageLength = messageLength;
		_rails = rails;
	}

	private int[]? _positionsBackingField;

	private int[] _positions
	{
		get
		{
			_positionsBackingField ??= InitializePositions();
			return _positionsBackingField;
		}
	}

	private int _currentIndex;

	private int[] InitializePositions()
	{
		int cycleLength = (_rails * 2) - 2;
		return Enumerable.Range(0, _messageLength)
			.Select(position => CalculatePosition(position, cycleLength))
			.ToArray();
	}

	private int CalculatePosition(int position, int cycleLength)
	{
		int remainder = position % cycleLength;
		return remainder < _rails
			? position % cycleLength
			: cycleLength - remainder;
	}

	internal int GetNextPosition()
		=> _positions[_currentIndex++];
}

// ReSharper disable once CheckNamespace
internal record DecodePair(char Character, int Index);