defmodule Tournament do
  alias Match
  alias TeamTally
  alias Table

  @doc """
  Given `input` lines representing two teams and whether the first of them won,
  lost, or reached a draw, separated by semicolons, calculate the statistics
  for each team's number of games played, won, drawn, lost, and total points
  for the season, and return a nicely-formatted string table.

  A win earns a team 3 points, a draw earns 1 point, and a loss earns nothing.

  Order the outcome by most total points for the season, and settle ties by
  listing the teams in alphabetical order.
  """
  @spec tally(input :: list(String.t())) :: String.t()
  def tally(input),
    do:
      input
      |> Enum.map(&Match.new(&1))
      |> Enum.map(&Match.to_outcome(&1))
      |> List.flatten()
      |> Enum.group_by(&elem(&1, 0))
      |> Enum.map(&TeamTally.new(&1))
      |> Table.new()
      |> Table.to_string()
end

defmodule Match do
  defstruct [:team_1_name, :team_2_name, :team_1_result]

  def new(data)
      when is_binary(data),
      do: new(String.split(data, ";"))

  def new(data)
      when is_list(data),
      do: try_new(data)

  defp try_new([team_1_name, team_2_name, team_1_result]),
    do: %Match{
      team_1_name: team_1_name,
      team_2_name: team_2_name,
      team_1_result: to_result(team_1_result)
    }

  defp try_new(_),
    do: nil

  defp to_result(team_1_result) do
    try do
      String.to_existing_atom(team_1_result)
    rescue
      _ -> nil
    end
  end

  def to_outcome(%{team_1_name: team_1_name, team_2_name: team_2_name, team_1_result: :win}),
    do: [
      {team_1_name, :win},
      {team_2_name, :loss}
    ]

  def to_outcome(%{team_1_name: team_1_name, team_2_name: team_2_name, team_1_result: :loss}),
    do: [
      {team_2_name, :win},
      {team_1_name, :loss}
    ]

  def to_outcome(%{team_1_name: team_1_name, team_2_name: team_2_name, team_1_result: :draw}),
    do: [
      {team_1_name, :draw},
      {team_2_name, :draw}
    ]

  def to_outcome(_),
    do: []
end

defmodule TeamTally do
  alias __MODULE__
  defstruct([:team_name, :wins, :draws, :losses])

  @points_by_result %{
    win: 3,
    draw: 1,
    loss: 0
  }
  @results Map.keys(@points_by_result)

  def new({team_name, matches}),
    do:
      matches
      |> Enum.group_by(&elem(&1, 0))
      |> Enum.map(&tally_results/1)
      |> List.flatten()
      |> Map.new()
      |> new(team_name)

  def new(%{win: wins, loss: losses, draw: draws}, team_name),
    do: %TeamTally{
      team_name: team_name,
      wins: wins,
      losses: losses,
      draws: draws
    }

  def new(%{} = tally_data, team_name),
    do:
      @results
      |> Enum.map(&fill(tally_data, &1))
      |> Map.new()
      |> new(team_name)

  defp fill(tally_data, result),
    do: {result, Map.get(tally_data, result, 0)}

  defp tally_results({_team_name, data}),
    do:
      data
      |> Enum.group_by(&elem(&1, 1))
      |> Enum.map(&convert_to_length/1)

  defp convert_to_length({key, data}),
    do: {key, length(data)}

  def to_map(%TeamTally{} = team_tally),
    do:
      team_tally
      |> Map.from_struct()
      |> Map.merge(%{
        matches_played: to_matches_played(team_tally),
        points: to_points(team_tally)
      })

  defp to_matches_played(%TeamTally{} = team_tally),
    do: team_tally.wins + team_tally.draws + team_tally.losses

  defp to_points(%TeamTally{} = team_tally),
    do:
      team_tally.wins * Map.get(@points_by_result, :win) +
        team_tally.draws * Map.get(@points_by_result, :draw)
end

defmodule Table do
  alias __MODULE__
  alias TeamTally

  defstruct([:data])

  @header "Team                           | MP |  W |  D |  L |  P\n"
  def new(data),
    do: %Table{data: data}

  #  defp row(%TeamTally{} = team_tally),
  #    do: row(TeamTally.to_map(team_tally))

  defp row(
         %{
           team_name: team_name,
           wins: wins,
           draws: draws,
           losses: losses,
           matches_played: matches_played,
           points: points
         } = data
       ),
       do:
         String.pad_trailing(team_name, 30) <>
           concat_to(matches_played) <>
           concat_to(wins) <>
           concat_to(draws) <>
           concat_to(losses) <>
           concat_to(points)

  defp concat_to(tally),
    do: " |#{String.pad_leading(Integer.to_string(tally), 3)}"

  def to_string(%Table{data: data}),
    do:
      data
      |> Enum.map(&TeamTally.to_map/1)
      |> Enum.sort(&tally_sorter/2)
      |> Enum.map(&row/1)
      |> Enum.join("\n")
      |> prefix_header()

  defp tally_sorter(tally1, tally2),
    do:
      tally1.points > tally2.points ||
        (tally1.points === tally2.points &&
           tally1.team_name < tally2.team_name)

  defp prefix_header(table_string),
    do: @header <> table_string
end
