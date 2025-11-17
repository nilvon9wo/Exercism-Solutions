using System.Collections.Generic;
using System.Linq;

public static class MatchingBrackets
{
	private static readonly Dictionary<char, char> openersByClosers =
		new()
		{
			{ ')' , '(' },
			{ ']' , '[' },
			{ '}' , '{' },
		};

	private static readonly HashSet<char> openers = openersByClosers.Values.ToHashSet();

	public static bool IsPaired(string input)
	{
		List<char> unclosed = new();
		foreach (char character in input)
		{
			if (openers.Contains(character))
			{
				unclosed.Add(character);
			}
			else if (openersByClosers.TryGetValue(character, out char opener))
			{
				if (unclosed.Any() && unclosed.Last() == opener)
				{
					unclosed.RemoveAt(unclosed.Count - 1);
				}
				else
				{
					return false;
				}
			}
		}

		return !unclosed.Any();
	}
}
