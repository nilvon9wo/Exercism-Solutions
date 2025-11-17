using System.Collections.Generic;
using System.Linq;

public static class ParallelLetterFrequency
{
	public static Dictionary<char, int> Calculate(IEnumerable<string> texts)
	{
		return texts
			.AsParallel()
			.Aggregate(
			new Dictionary<char, int>(),
			(outerSeed, text) =>
				{
					Dictionary<char, int> innerDictionary = Calculate(text);
					foreach (KeyValuePair<char, int> kvp in innerDictionary)
					{
						(char letter, int count) = kvp;
						if (!outerSeed.ContainsKey(letter))
						{
							outerSeed[letter] = 0;
						}

						outerSeed[letter] += count;
					}

					return outerSeed;
				}
			);
	}

	private static Dictionary<char, int> Calculate(string text)
	{
		return text.Aggregate(
				new Dictionary<char, int>(),
				(innerSeed, character) =>
				{
					if (char.IsLetter(character))
					{
						char lowerCaseCharacter = char.ToLower(character);
						if (!innerSeed.ContainsKey(lowerCaseCharacter))
						{
							innerSeed[lowerCaseCharacter] = 0;
						}

						innerSeed[lowerCaseCharacter]++;
					}

					return innerSeed;
				}
			);
	}
}