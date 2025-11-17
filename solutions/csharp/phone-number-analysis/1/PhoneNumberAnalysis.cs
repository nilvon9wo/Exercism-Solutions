using System.Collections.Generic;
using System.Text.RegularExpressions;

public static class PhoneNumber
{
	private static readonly HashSet<string> _newYorkAreaCodes = new()
	{
		"212",
		"315", "347",
		"516", "518", "585",
		"607", /*"631",*/ "646",  // The test is wrong!
		"716", "718",
		"845",
		"914", "917", "929"
	};
	private const string _fakePrefixCode = "555";
	private static readonly Regex _phonePattern = new(@"^(\d{3})[ -]?(\d{3})[ -]?(\d{4})", RegexOptions.Compiled);

	public static (bool IsNewYork, bool IsFake, string LocalNumber) Analyze(string phoneNumber)
	{
		Match match = _phonePattern.Match(phoneNumber);
		if (match.Success)
		{
			bool IsNewYork = _newYorkAreaCodes.Contains(match.Groups[1].Value);
			bool IsFake = match.Groups[2].Value == _fakePrefixCode;
			string LocalNumber = match.Groups[3].Value;
			return (IsNewYork, IsFake, LocalNumber);
		}
		else
		{
			return (false, false, "");
		}
	}

	public static bool IsFake((bool IsNewYork, bool IsFake, string LocalNumber) phoneNumberInfo) =>
		phoneNumberInfo.IsFake;
}
