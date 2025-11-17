using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

public static class Tournament
{
	private const string _matchSeparator = "\n";

	public static void Tally(Stream inStream, Stream outStream) =>
		new ScoreSheet(ExtractOutcomes(inStream))
			.ToString()
			.Write(outStream);

	private static List<TournamentDiff> ExtractOutcomes(Stream inStream)
	{
		List<TournamentOutcome> outcomes = new StreamReader(inStream)
					.ReadToEnd()
					.Split(_matchSeparator)
					.Select(TournamentOutcome.From)
					.Where(x => x != null)
					.ToList();

		return outcomes.Any()
				? outcomes
					.SelectMany(x => x.ToDiffs())
					.ToList()
				: (new());
	}
}

public class ScoreSheet
{
	private readonly List<TournamentDiff> _diffs;

	public ScoreSheet(List<TournamentDiff> diffs) =>
		_diffs = diffs ?? throw new ArgumentNullException(nameof(diffs));

	public const string _matchSeparator = "\n";
	private const string _header = "Team                           | MP |  W |  D |  L |  P";

	public override string ToString()
	{
		List<string> rows = new() { _header };
		if (_diffs.Any())
		{
			List<string> results = _diffs
				.GroupBy(diff => diff.TeamName, diff => diff)
				.ToDictionary(
					pair => pair.Key,
					pair =>
					{
						Dictionary<TournamentResult, int> countByResult = pair.ToCountDictionary(x => x.TeamResult);
						TeamResult teamResult = new(pair.Key)
						{
							Wins = countByResult.GetOrZero(TournamentResult.Win),
							Losses = countByResult.GetOrZero(TournamentResult.Loss),
							Draws = countByResult.GetOrZero(TournamentResult.Draw),
						};
						return teamResult;
					})
				.OrderByDescending(x => x.Value.Points)
				.ThenBy(x => x.Value.TeamName)
				.Select(x => Format(x.Value))
				.ToList();
			rows.AddRange(results);
		}

		return string.Join("\n", rows);
	}

	private string Format(TeamResult value) =>
		$"{value.TeamName,-31}| {value.MatchPlayed,2} | {value.Wins,2} | {value.Draws,2} | {value.Losses,2} | {value.Points,2}";
}

public class TeamResult
{
	public TeamResult(string teamName)
	{
		if (string.IsNullOrWhiteSpace(teamName))
		{
			throw new ArgumentException($"'{nameof(teamName)}' cannot be null or whitespace.", nameof(teamName));
		}

		TeamName = teamName;
	}

	public string TeamName { get; init; }

	public int Wins { get; init; }

	public int Draws { get; init; }

	public int Losses { get; init; }

	public int MatchPlayed =>
		Wins + Losses + Draws;

	public int Points =>
		(Wins * 3) + Draws;
}

public class TournamentOutcome
{
	private const string _dataSeparator = ";";

	public string Team1 { get; private init; }
	public string Team2 { get; private init; }
	public TournamentResult Result { get; private init; }

	public static TournamentOutcome From(string input)
	{
		if (string.IsNullOrWhiteSpace(input))
		{
			return null;
		}

		string[] datum = input.Split(_dataSeparator);
		return new()
		{
			Team1 = datum[0],
			Team2 = datum[1],
			Result = datum[2].ToTournamentResult()
		};
	}

	public TournamentDiff[] ToDiffs() =>
		new TournamentDiff[] {
			new TournamentDiff(Team1, Result),
			new TournamentDiff(Team2, Result.ToOpposite())
		};
}

public class TournamentDiff
{
	public TournamentDiff(string teamName, TournamentResult tournamentResult)
	{
		if (string.IsNullOrEmpty(teamName))
		{
			throw new ArgumentException($"'{nameof(teamName)}' cannot be null or empty.", nameof(teamName));
		}

		TeamName = teamName;
		TeamResult = tournamentResult;
	}

	public string TeamName { get; private init; }
	public TournamentResult TeamResult { get; private init; }
}

public enum TournamentResult
{
	Draw = 0,
	Loss = -1,
	Win = 1,
}

public static class TournamentResultExtensions
{
	public static TournamentResult ToOpposite(this TournamentResult result) =>
		(TournamentResult)(-(int)result);
}

public static class StringExtensions
{
	public static TournamentResult ToTournamentResult(this string value) =>
		Enum.Parse<TournamentResult>(value, true);

	public static void Write(this string value, Stream outStream)
	{
		StreamWriter writer = new(outStream);
		writer.Write(value);
		writer.Flush();
		outStream.Position = 0;
	}
}

public static class IEnumerableExtensions
{
	public static Dictionary<TKey, int> ToCountDictionary<TCountable, TKey>(this IEnumerable<TCountable> records, Func<TCountable, TKey> keySelector) =>
		records
		.GroupBy(keySelector)
		.Select(value => new
		{
			Value = value.Key,
			Count = value.Count()
		})
		.ToDictionary(
			group => group.Value,
			group => group.Count
		);

	public static int GetOrZero<TKey>(this Dictionary<TKey, int> countDictionary, TKey key) =>
		countDictionary.GetValueOrDefault(key, 0);
}

