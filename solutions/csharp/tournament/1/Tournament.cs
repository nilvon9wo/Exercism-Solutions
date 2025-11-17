using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Linq;

public static class Tournament
{
	private const string _matchSeparator = "\n";

	public static void Tally(Stream inStream, Stream outStream)
	{
		List<TournamentOutcome> outcomes = ExtractOutcomes(inStream);
		new ScoreSheet(outcomes)
			.ToString()
			.Write(outStream);
	}

	private static string CreateTally(List<TournamentOutcome> outcomes) =>
		throw new NotImplementedException();

	private static List<TournamentOutcome> ExtractOutcomes(Stream inStream) =>
		new StreamReader(inStream)
			.ReadToEnd()
			.Split(_matchSeparator)
			.Select(TournamentOutcome.From)
			.Where(x => x != null)
			.ToList();
}

public class ScoreSheet
{
	private readonly List<TournamentOutcome> _outcomes;

	public ScoreSheet(List<TournamentOutcome> outcomes) =>
		_outcomes = outcomes ?? throw new ArgumentNullException(nameof(outcomes));

	public const string _matchSeparator = "\n";
	private const string _header = "Team                           | MP |  W |  D |  L |  P";

	private Dictionary<string, TeamResult> resultByTeams;

	private Dictionary<string, TeamResult> _resultByTeams
	{
		get
		{
			if (resultByTeams == null)
			{
				resultByTeams = new();
				foreach (TournamentOutcome outcome in _outcomes)
				{
					Add(outcome.Normalize());
				}
			}

			return resultByTeams;
		}
	}

	private void Add(TournamentOutcome outcome)
	{
		_ = Get(outcome.Team1)
			.Add(outcome);

		_ = Get(outcome.Team2)
			.Add(outcome);
	}

	private TeamResult Get(string teamName)
	{
		if (!resultByTeams.TryGetValue(teamName, out TeamResult teamResult))
		{
			teamResult = new TeamResult(teamName);
			resultByTeams[teamName] = teamResult;
		}

		return teamResult;

	}

	public override string ToString()
	{
		List<string> rows = new() { _header };
		List<string> results = _resultByTeams.Values
			.OrderByDescending(x => x.Points)
			.ThenBy(x => x.TeamName)
			.Select(x => x.ToString())
			.ToList();
		rows.AddRange(results);

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

	public int Wins { get; set; } = 0;

	public int Draws { get; set; } = 0;

	public int Losses { get; set; } = 0;

	public int MatchPlayed =>
		Wins + Losses + Draws;

	public int Points =>
		(Wins * 3) + Draws;

	public TeamResult Add(TournamentOutcome outcome)
	{
		switch (outcome.Result)
		{
			case TournamentResult.Draw:
				Draws++;
				break;

			case TournamentResult.Win:
				if (TeamName == outcome.Team1)
				{
					Wins++;
				}
				else
				{
					Losses++;
				}

				break;

			case TournamentResult.Loss:
				if (TeamName == outcome.Team1)
				{
					Losses++;
				}
				else
				{
					Wins++;
				}

				break;
		}

		return this;
	}

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

public enum TournamentResult
{
	[Description("draw")]
	Draw,

	[Description("loss")]
	Loss,

	[Description("win")]
	Win,
}

public static class StringExtensions
{
	public static TournamentResult ToTournamentResult(this string value) =>
		value switch
		{
			"draw" => TournamentResult.Draw,
			"loss" => TournamentResult.Loss,
			"win" => TournamentResult.Win,
			_ => throw new ArgumentException("Invalid result.", nameof(value)),
		};

	public static void Write(this string value, Stream outStream)
	{
		StreamWriter writer = new(outStream);
		writer.Write(value);
		writer.Flush();
		outStream.Position = 0;
	}
}

