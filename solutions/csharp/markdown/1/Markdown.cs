using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

// ReSharper disable once CheckNamespace

internal static class HtmlTagHelper
{
	private const string _openingTagStart = "<";
	private const string _closingTagStart = "</";
	private const string _tagEnd = ">";

	internal const string StartOfList = "<ul>";
	internal const string EndOfList = "</ul>";
	private const string _headerTag = "h";
	private const string _paragraphTag = "p";
	private const string _listItemTag = "li";

	internal static string ToHeaderWrapped(this string markdown, int count)
		=> markdown[(count + 1)..]
			.ToTagWrapped(_headerTag + count);

	internal static string ToParagraphWrapped(this string markdown)
		=> markdown
			.ToTagWrapped(_paragraphTag);

	internal static string ToListItemWrapped(this string markdown)
		=> markdown
			.ToTagWrapped(_listItemTag);

	internal static string ToTagWrapped(this string? text, string tag)
		=> $"{ToOpeningTag(tag)}{text}{ToClosingTag(tag)}";

	private static string ToOpeningTag(this string tagContent)
		=> $"{_openingTagStart}{tagContent}{_tagEnd}";

	private static string ToClosingTag(this string tagContent)
		=> $"{_closingTagStart}{tagContent}{_tagEnd}";
}

//=======================================================================

// ReSharper disable once CheckNamespace
public static class Markdown
{
	private const char _endOfLine = '\n';

	public static string Parse(string markdown)
	{
		if (markdown is null)
		{
			throw new ArgumentNullException(nameof(markdown));
		}

		List<ParseResult> processedLines = ProcessMarkdown(markdown);
		string transcriptionResult = ConcatenateLineResults(processedLines);
		if (IsStillList(processedLines))
		{
			transcriptionResult += HtmlTagHelper.EndOfList;
		}

		return transcriptionResult;
	}

	private static List<ParseResult> ProcessMarkdown(string markdown)
		=> markdown.Split(_endOfLine)
			.Aggregate(
				new List<ParseResult>(),
				(processedLines, line) =>
				{
					bool isStillList = IsStillList(processedLines);
					processedLines.Add(ParseLine(line, isStillList));
					return processedLines;
				}
			);

	private static bool IsStillList(IReadOnlyCollection<ParseResult> processedLines)
		=> processedLines.Any()
		   && processedLines.Last()
			   .IsListAfter;

	private static ParseResult ParseLine(string markdown, bool isList)
	{
		int headerLevel = StyleTranslator.DetermineHeaderLevel(markdown);
		return headerLevel is not 0 and <= 6
			? new()
			{
				TranspiledMarkup = StyleTranslator.TranslateHeader(markdown, headerLevel, isList),
				IsListAfter = isList,
			}
			: StyleTranslator.IsLineItem(markdown)
				? new() { TranspiledMarkup = StyleTranslator.TranslateListItem(markdown, isList), IsListAfter = true }
				: new()
				{
					TranspiledMarkup = StyleTranslator.TranslateParagraph(markdown, isList)
									   ?? throw new ArgumentException("Invalid markdown"),
					IsListAfter = false,
				};
	}

	private static string ConcatenateLineResults(IEnumerable<ParseResult> parseResults)
	{
		IEnumerable<string> lineResults = parseResults.Select(x => x.TranspiledMarkup);
		return string.Concat(lineResults);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal readonly struct ParseResult
{
	public required string TranspiledMarkup { get; init; }
	public required bool IsListAfter { get; init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class StyleTranslator
{
	private const string _header = "#";
	private const string _listItem = "*";
	private const string _bold = "__";
	private const string _italics = "_";

	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;

	private static readonly Dictionary<string, string> _htmlByMarkdown
		= new() { { _bold, "strong" }, { _italics, "em" } };

	internal static int DetermineHeaderLevel(string markdown)
		=> markdown.TakeWhile(c => c == _header[0])
			.Count();

	internal static string TranslateHeader(string markdown, int count, bool isList)
	{
		string header = markdown.ToHeaderWrapped(count);
		return isList
			? HtmlTagHelper.EndOfList + header
			: header;
	}

	internal static bool IsLineItem(string markdown)
		=> markdown.StartsWith(_listItem, _invariantCulture);

	internal static string TranslateListItem(string markdown, bool isList)
	{
		string listItem = StylizeFonts(markdown[2..])
			.ToListItemWrapped();
		return isList
			? listItem
			: HtmlTagHelper.StartOfList + listItem;
	}

	internal static string TranslateParagraph(string markdown, bool isList)
	{
		string parsedText = StylizeFonts(markdown)
			.ToParagraphWrapped();
		return !isList
			? parsedText
			: HtmlTagHelper.EndOfList + parsedText;
	}

	private static string StylizeFonts(string markdown)
	{
		string stylizedMarkdown = MakeBold(markdown);
		return MakeItalicized(stylizedMarkdown);
	}

	private static string MakeBold(string markdown)
		=> Replace(markdown);

	private static string MakeItalicized(string markdown)
		=> Replace(markdown);

	private static string Replace(string markdown)
		=> _htmlByMarkdown.Aggregate(
			markdown,
			(currentMarkdown, replacementPair) =>
			{
				(string markdownNotation, string htmlTag) = replacementPair;
				string pattern = $"{markdownNotation}(.+){markdownNotation}";
				string replacement = "$1".ToTagWrapped(htmlTag);
				return Regex.Replace(currentMarkdown, pattern, replacement);
			}
		);
}

//=======================================================================