using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

// ReSharper disable once CheckNamespace
public static class Diamond
{
	private const char _letterA = 'A';
	private const char _endOfLine = '\n';

	public static string Make(char target)
	{
		int size = target - _letterA + 1;
		List<char> letters = Enumerable.Range(_letterA, size)
			.Select(letter => (char)letter)
			.ToList();

		SizeInfo sizeInfo = new(size);
		StringBuilder diamondBottom = BuildDiamondBottom(sizeInfo, letters);
		return BuildDiamondTop(sizeInfo, letters)
			.Append(diamondBottom)
			.ToString()
			.TrimEnd(_endOfLine);
	}

	private static StringBuilder BuildDiamondTop(SizeInfo sizeInfo, IList<char> letters)
		=> letters.Aggregate(
			new StringBuilder(),
			(diamond, letter) =>
			{
				int index = letters.IndexOf(letter);
				StringBuilder row = GenerateDiamondRow(letter, index, sizeInfo);
				return diamond.Append(row)
					.Append(_endOfLine);
			}
		);

	private static StringBuilder BuildDiamondBottom(SizeInfo sizeInfo, IList<char> letters)
		=> Enumerable.Range(1, sizeInfo.RowSize)
			.Aggregate(
				new StringBuilder(),
				(diamond, index) =>
				{
					int distanceFromCenter = sizeInfo.DistanceFromCenter(index);
					char letter = letters[distanceFromCenter];
					StringBuilder row = GenerateDiamondRow(letter, distanceFromCenter, sizeInfo);
					return diamond.Append(row)
						.Append(_endOfLine);
				}
			);

	private static StringBuilder GenerateDiamondRow(char letter, int index, SizeInfo sizeInfo)
		=> letter == _letterA
			? GenerateDiamondRowForA(sizeInfo)
			: GenerateDiamondRowForNonA(letter, index, sizeInfo);

	private static StringBuilder GenerateDiamondRowForA(SizeInfo dimensions)
	{
		string padding = new(' ', dimensions.RowSize);
		return new StringBuilder()
			.Append(padding)
			.Append(_letterA)
			.Append(padding);
	}

	private static StringBuilder GenerateDiamondRowForNonA(char letter, int index, SizeInfo sizeInfo)
	{
		int distanceFromCenter = sizeInfo.DistanceFromCenter(index);
		int padding = Math.Abs(distanceFromCenter);
		string sideSpaces = new(' ', padding);
		string middleSpaces = GenerateMiddleSpaces(index);
		return new StringBuilder().Append(sideSpaces)
			.Append(letter)
			.Append(middleSpaces)
			.Append(letter)
			.Append(sideSpaces);
	}

	private static string GenerateMiddleSpaces(int index)
		=> new(' ', (index * 2) - 1);
}

internal readonly struct SizeInfo
{
	internal SizeInfo(int size)
		=> _size = size;

	private int _size { get; }

	internal int RowSize
		=> _size - 1;

	internal int DistanceFromCenter(int index)
		=> RowSize - index;
}