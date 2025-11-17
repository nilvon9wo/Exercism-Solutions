using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

public static class Identifier
{
	private static readonly Regex GreekPattern = new("[\u0370-\u03FF]", RegexOptions.Compiled);

	public static string Clean(string identifier)
	{
		if (string.IsNullOrEmpty(identifier))
		{
			return "";
		}

		string cleanIdentifier = identifier
			.Replace(" ", "_")
			.Select(x => char.IsControl(x) ? "CTRL" : x.ToString())
			.Aggregate((seed, part) => seed + part);

		string camelIdenfitier = ConvertDashToCamelCase(cleanIdentifier);
		return RemoveIllegalCharacters(camelIdenfitier);
	}

	private static string ConvertDashToCamelCase(string input)
	{
		StringBuilder stringBuilder = new();
		bool caseFlag = false;
		foreach (char character in input)
		{
			if (character == '-')
			{
				caseFlag = true;
			}
			else if (caseFlag || char.IsUpper(character))
			{
				_ = stringBuilder.Append(char.ToUpper(character));
				caseFlag = false;
			}
			else
			{
				_ = stringBuilder.Append(char.ToLower(character));
			}
		}

		return stringBuilder.ToString();
	}

	public static string RemoveIllegalCharacters(this string str)
	{
		StringBuilder stringBuilder = new();
		foreach (char character in str)
		{
			if (IsLegalLetter(character) || character == '_')
			{
				_ = stringBuilder.Append(character);
			}
		}

		return stringBuilder.ToString();
	}

	private static bool IsLegalLetter(char character) =>
		char.IsLetter(character)
			&& !IsGreekLowercase(character);

	private static bool IsGreekLowercase(char character) =>
		char.IsLower(character)
			&& IsGreek(character);

	private static bool IsGreek(char character) =>
		GreekPattern.Match(character.ToString())
			.Success;
}
