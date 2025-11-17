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

	private static List<TournamentDiff> ExtractOutcomes(Stream inStream) =>
		new StreamReader(inStream)
			.ReadToEnd()
			.Split(_matchSeparator)
			.Select(TournamentOutcome.From)
			.SelectMany(TournamentDiff.From)
			.ToList();
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
				.ToDictionary(pair => pair.Key,
					pair =>
					{
						List<TournamentDiff> teamDiffs = pair.ToList();
						TeamResult teamResult = new(pair.Key)
						{
							Wins = teamDiffs.Count(x => x.TeamResult == TournamentResult.Win),
							Losses = teamDiffs.Count(x => x.TeamResult == TournamentResult.Loss),
							Draws = teamDiffs.Count(x => x.TeamResult == TournamentResult.Draw),
						};
						return teamResult;
					})
				.OrderByDescending(x => x.Value.Points)
				.ThenBy(x => x.Value.TeamName)
				.Select(x => x.Value.ToString())
				.ToList();
			rows.AddRange(results);
		}

		return string.Join("\n", rows);
	}
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

	public override string ToString() =>
		$"{TeamName,-31}| {MatchPlayed,2} | {Wins,2} | {Draws,2} | {Losses,2} | {Points,2}";
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

	public TournamentOutcome Normalize() =>
		Result != TournamentResult.Loss
			? this
			: new TournamentOutcome()
			{
				Team1 = Team2,
				Team2 = Team1,
				Result = TournamentResult.Win,
			};
}

public class TournamentDiff
{
	private TournamentDiff(string teamName, TournamentResult tournamentResult)
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

	public static TournamentDiff[] From(TournamentOutcome outcome) =>
		outcome?.Result switch
		{
			TournamentResult.Win =>
				new[] {
					new TournamentDiff(outcome.Team1, TournamentResult.Win),
					new TournamentDiff(outcome.Team2, TournamentResult.Loss)
				},

			TournamentResult.Loss =>
				new[] {
					new TournamentDiff(outcome.Team1, TournamentResult.Loss),
					new TournamentDiff(outcome.Team2, TournamentResult.Win)
				},

			TournamentResult.Draw =>
				new[] {
					new TournamentDiff(outcome.Team1, TournamentResult.Draw),
					new TournamentDiff(outcome.Team2, TournamentResult.Draw)
				},

			_ =>
				Array.Empty<TournamentDiff>()
		}
		?? Array.Empty<TournamentDiff>();
}

public enum TournamentResult
{
	Draw,
	Loss,
	Win,
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

