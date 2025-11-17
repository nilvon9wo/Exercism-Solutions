defmodule MatchingBrackets do
  @doc """
  Checks that all the brackets and braces in the string are matched correctly, and nested correctly
  """

  @brackets_map %{
    "[" => "]",
    "{" => "}",
    "(" => ")"
  }

  @openers Map.keys(@brackets_map)
  @closers Map.values(@brackets_map)

  @spec check_brackets(String.t()) :: boolean
  def check_brackets(string) do
    string
    |> remove_noise()
    |> String.graphemes()
    |> Enum.reduce([], &match/2)
    |> empty_list?()
  end

  defp remove_noise(string) do
    brackets = Enum.join(@openers ++ @closers, "\\")
    {:ok, regex} = Regex.compile("[^\\" <> brackets <> "]")
    String.replace(string, regex, "")
  end

  defp match(character, unmatched_openers) do
    cond do
      character === @brackets_map[List.first(unmatched_openers)] ->
        tl(unmatched_openers)

      true ->
        [character | unmatched_openers]
    end
  end

  defp empty_list?(list),
    do: list === []
end
