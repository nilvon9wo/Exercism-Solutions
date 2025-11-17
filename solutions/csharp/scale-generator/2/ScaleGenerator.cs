using System;
using System.Collections.Generic;
using System.Linq;
public static class ScaleGenerator
{
	public static string[] Chromatic(string tonic)
	{
		List<string> tonicNotes = WesternScales.Select(tonic);
		int tonicIndex = FindTonicIndex(tonic, tonicNotes);
		IEnumerable<string> requiredNots = tonicNotes.Take(tonicIndex);
		return tonicNotes.Concat(requiredNots)
			.Skip(tonicIndex)
			.ToArray();
	}

	public static string[] Interval(string tonic, string intervalPattern)
	{
		List<string> tonicNotes = WesternScales.Select(tonic);
		int tonicIndex = FindTonicIndex(tonic, tonicNotes);
		List<string> requiredNotes = intervalPattern.Select(stepCode =>
				TonicNoteGenerator.GetNextNote(tonicNotes, ref tonicIndex, stepCode)
			)
			.ToList();

		string firstNote = tonicNotes.Find(x => IsSameTonic(tonic, x));
		requiredNotes.Insert(0, firstNote);

		return requiredNotes.ToArray();
	}

	private static int FindTonicIndex(string tonic, List<string> tonicNotes) =>
		tonicNotes.FindIndex(x => IsSameTonic(tonic, x));

	private static bool IsSameTonic(string tonic, string x) =>
		x.ToLowerInvariant() == tonic.ToLowerInvariant();

}

public static class TonicNoteGenerator
{
	private static readonly Dictionary<char, int> _stepSizeByCode = new()
	{
		{ 'A' , 3 },
		{ 'M' , 2 },
		{ 'm' , 1 },
	};

	public static string GetNextNote(List<string> tonicNotes, ref int tonicIndex, char stepCode)
	{
		tonicIndex += GetStepSize(stepCode);
		tonicIndex %= tonicNotes.Count;
		return tonicNotes[tonicIndex];
	}

	private static int GetStepSize(char stepCode) =>
		_stepSizeByCode.TryGetValue(stepCode, out int stepSize)
			? stepSize
			: default;
}

public static class WesternScales
{
	private static readonly List<string> ChromaticsSharp = new() {
		"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

	private static readonly List<string> ChromaticsFlat = new() {
		"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab",
	};

	private static readonly List<string> TonicsSharp = new() {
		"C", "G", "D", "A", "E", "B", "F#", "a", "e", "b", "f#", "c#", "g#", "d#"
	};

	private static readonly List<string> TonicsFlat = new() {
		"F", "Bb", "Eb", "Ab", "Db", "Gb ", "d", "g", "c", "f", "bb", "eb"
	};

	public static List<string> Select(string tonic) =>
		tonic switch
		{
			_ when TonicsFlat.Contains(tonic) =>
					ChromaticsFlat,

			_ when TonicsSharp.Contains(tonic) =>
					ChromaticsSharp,

			_ => throw new ArgumentOutOfRangeException($"Invalid tonic {tonic}"),
		};

}