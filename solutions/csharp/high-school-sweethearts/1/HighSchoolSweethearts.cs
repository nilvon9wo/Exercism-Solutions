using System;
using System.Globalization;

public static class HighSchoolSweethearts
{
	private const int _singleLineLength = 61;

	public static string DisplaySingleLine(string studentA, string studentB) =>
		Centre($"{studentA} ♡ {studentB}");

	private static string Centre(string input)
	{
		int inputLength = input.Length;
		int paddingNeeded = _singleLineLength - inputLength;
		int paddingNeedPerSide = (paddingNeeded / 2) - 1;
		return input
			.PadLeft(inputLength + paddingNeedPerSide)
			.PadRight(_singleLineLength);
	}

	public static string DisplayBanner(string studentA, string studentB) =>
@$"
     ******       ******
   **      **   **      **
 **         ** **         **
**            *            **
**                         **
**     {studentA.Trim()}  +  {studentB.Trim()}     **
 **                       **
   **                   **
     **               **
       **           **
         **       **
           **   **
             ***
              *
";

	public static string DisplayGermanExchangeStudents(string studentA, string studentB, DateTime start, float hours)
	{
		CultureInfo cultureInfo = new("de-DE");
		string dateString = start.ToString("d", cultureInfo);
		string timeString = hours.ToString("N2", cultureInfo);
		return $"{studentA} and {studentB} have been dating since {dateString} - that's {timeString} hours";
	}
}
