using System.Linq;
using System.Text.RegularExpressions;

public class LogParser
{
	private static readonly Regex LogPattern = new(@"^\[(\w*)\]\s*(.*)", RegexOptions.Compiled);
	private static readonly Regex SplitPattern = new(@"<.+?>", RegexOptions.Compiled);
	private static readonly Regex QuotedPasswordPattern = new("\\\".*(password).*\\\"", RegexOptions.Compiled | RegexOptions.IgnoreCase);
	private static readonly Regex PasswordPrefixPattern = new(@"((password)\w+)[\s\S]*?$", RegexOptions.Compiled | RegexOptions.IgnoreCase);
	private static readonly Regex EndOfLinePattern = new(@"(end-of-line)\d*", RegexOptions.Compiled);

	public bool IsValidLine(string text) =>
		LogPattern.IsMatch(text);

	public string[] SplitLogLine(string text) =>
		SplitPattern.Split(text);

	public int CountQuotedPasswords(string lines) =>
		QuotedPasswordPattern.Matches(lines)
			.Count();

	public string RemoveEndOfLineText(string line) =>
		EndOfLinePattern.Replace(line, "");

	public string[] ListLinesWithPasswords(string[] lines) =>
		lines.Select(line =>
		{
			Match match = PasswordPrefixPattern.Match(line);
			return match.Success
				? $"{match.Groups[1].Value}: {line}"
				: $"--------: {line}";
		})
		.ToArray();
}
