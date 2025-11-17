using System.Collections.Generic;
using System.Linq;

public static class Etl
{
	public static Dictionary<string, int> Transform(Dictionary<int, string[]> lettersByPoints) =>
		lettersByPoints.Aggregate(
			new Dictionary<string, int>(),
			(seed, lettersByPointPair) =>
			{
				(int points, string[] letters) = lettersByPointPair;
				foreach (string letter in letters)
				{
					seed[letter.ToLower()] = points;
				}

				return seed;
			}
		);
}