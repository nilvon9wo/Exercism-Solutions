using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class Grep
{
	private const char _endOfLine = '\n';

	public static string Match(string pattern, string? flags, string[] files)
	{
		if (files is null)
		{
			throw new ArgumentNullException(nameof(files));
		}

		if (files.Length == 0)
		{
			return "";
		}

		flags ??= string.Empty;
		IEnumerable<LineMatch> matchingLines = SearchFilesForMatchingLines(pattern, files, flags);
		IEnumerable<string> lineData = SwitchHandler.ShouldOnlyOutputFilenameOfMatches(flags)
			? matchingLines.Select(x => x.Filename)
				.ToHashSet()
			: matchingLines.Select(x => x.ToString(flags, files.Length > 1));
		return string.Join(_endOfLine, lineData);
	}

	private static IEnumerable<LineMatch> SearchFilesForMatchingLines(
		string pattern,
		IEnumerable<string> files,
		string flags
	)
		=> files.Aggregate(
			new List<LineMatch>(),
			(matchingLines, file) =>
			{
				try
				{
					IEnumerable<LineMatch> fileMatchingLines =
						SearchFileForMatchingLines(pattern, file, flags);
					matchingLines.AddRange(fileMatchingLines);
				}
				catch (FileNotFoundException)
				{
					// Ignore the case where the file doesn't exist.
				}

				return matchingLines;
			}
		);

	private static IEnumerable<LineMatch> SearchFileForMatchingLines(string pattern, string file, string flags)
	{
		Func<string, bool> stringChecker = SwitchHandler.CreateStringCheckerFunction(pattern, flags);
		return File.ReadAllLines(file)
			.Select((line, index) => new { Line = line, LineNumber = index + 1 })
			.Where(item => stringChecker(item.Line))
			.Select(
				item => new LineMatch()
				{
					Filename = file,
					LineNumber = item.LineNumber,
					Line = item.Line,
				}
			);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal record LineMatch
{
	internal required string Filename { get; init; }
	internal required int LineNumber { get; init; }
	internal required string Line { get; init; }

	public string ToString(string flags, bool includeFilename = false)
	{
		string lineString = SwitchHandler.IncludeLineNumbers(flags)
			? $"{LineNumber}:{Line}"
			: Line;

		return includeFilename
			? $"{Filename}:{lineString}"
			: lineString;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class SwitchHandler
{
	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;
	private const string _prependLineNumberFlag = "-n";
	private const string _outputOnlyNamesOfFilesWithMatchesFlags = "-l";
	private const string _caseInsensitiveFlag = "-i";
	private const string _collectLinesWithoutMatchesFlag = "-v";
	private const string _matchEntireLineFlag = "-x";

	internal static Func<string, bool> CreateStringCheckerFunction(string pattern, string flags)
	{
		flags ??= "";
		StringComparison comparison = SelectStringComparison(flags);
		Func<string, bool> stringChecker = MustMatchEntireLine(flags)
			? input => input.Equals(pattern, comparison)
			: input => input.Contains(pattern, comparison);
		return ShouldInvertMatches(flags)
			? input => !stringChecker(input)
			: stringChecker;
	}

	private static StringComparison SelectStringComparison(string flags)
		=> IgnoreCase(flags)
			? StringComparison.OrdinalIgnoreCase
			: StringComparison.InvariantCulture;

	private static bool IgnoreCase(string flags)
		=> flags.Contains(_caseInsensitiveFlag, _invariantCulture);

	internal static bool IncludeLineNumbers(string flags)
		=> flags.Contains(_prependLineNumberFlag, _invariantCulture);

	internal static bool ShouldOnlyOutputFilenameOfMatches(string flags)
		=> flags.Contains(_outputOnlyNamesOfFilesWithMatchesFlags, _invariantCulture);

	private static bool ShouldInvertMatches(string flags)
		=> flags.Contains(_collectLinesWithoutMatchesFlag, _invariantCulture);

	private static bool MustMatchEntireLine(string flags)
		=> flags.Contains(_matchEntireLineFlag, _invariantCulture);
}

//=======================================================================