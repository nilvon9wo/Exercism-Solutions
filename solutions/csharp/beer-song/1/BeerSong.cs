using System.Linq;

public static class BeerSong
{
	private static readonly string NoMoreBottleVerse = """
			No more bottles of beer on the wall, no more bottles of beer.
			Go to the store and buy some more, 99 bottles of beer on the wall.
			""".Replace("\r", "");

	public static string Recite(int startBottleCount, int takeDown)
	{
		string[] verses = Enumerable.Range(startBottleCount - takeDown + 1, takeDown)
			.Reverse()
			.Select(x => x > 0
					? Recite(x)
					: NoMoreBottleVerse
			)
			.ToArray();

		return string.Join("\n\n", verses);
	}

	private static string Recite(int startBottleCount) =>
		$$"""
			{{ReciteFirstLine(startBottleCount)}}
			{{ReciteSecondLine(startBottleCount)}}
			"""
			.Replace("\r", "");

	private static string ReciteFirstLine(int startBottleCount)
	{
		string startBottleNoun = GetBottleNoun(startBottleCount);
		return $"{startBottleCount} {startBottleNoun} of beer on the wall, {startBottleCount} {startBottleNoun} of beer.";
	}

	private static string ReciteSecondLine(int startBottleCount)
	{
		int remainingBottleCount = startBottleCount - 1;
		if (remainingBottleCount > 0)
		{
			string remainingBottleNoun = GetBottleNoun(remainingBottleCount);
			return $"Take one down and pass it around, {remainingBottleCount} {remainingBottleNoun} of beer on the wall.";
		}
		else
		{
			return "Take it down and pass it around, no more bottles of beer on the wall.";
		}
	}

	private static string GetBottleNoun(int count) => (count == 1)
				? "bottle"
				: "bottles";
}