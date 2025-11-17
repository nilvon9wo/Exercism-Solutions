using System.Collections.Generic;
using System.Linq;

public static class DictionaryExtensions
{
	public static List<List<int>> Tokenise(this Dictionary<char, int> tokens, string equation) =>
		equation.Replace("==", "=")
			.Replace(" ", "")
			.Split("+=".ToCharArray())
			.Select(item =>
				item.Select(character => tokens[character])
					.ToList()
			)
			.ToList();
}
