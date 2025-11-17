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
  def check_brackets(string),
    do:
      string
      |> String.split("")
      |> check_brackets(_openers = [])

  def check_brackets(characters, openers)
      when length(characters) === 0,
      do: length(openers) === 0

  def check_brackets([first_character | remaining_characters] = _characters, openers),
    do:
      first_character
      |> determine_role
      |> check_brackets(first_character, remaining_characters, openers)

  def check_brackets(:opener, first_character, remaining_characters, openers),
    do: check_brackets(remaining_characters, [first_character | openers])

  def check_brackets(:closer, _first_character, _remaining_characters, openers)
      when length(openers) === 0,
      do: false

  def check_brackets(
        :closer,
        first_character,
        remaining_characters,
        [last_opener | remaining_openers] = _openers
      ) do
    if is_matching_closer?(last_opener, first_character),
      do: check_brackets(remaining_characters, remaining_openers),
      else: false
  end

  def check_brackets(:other, _first_character, remaining_characters, openers),
    do: check_brackets(remaining_characters, openers)

  defp determine_role(character) do
    cond do
      Enum.member?(@openers, character) ->
        :opener

      Enum.member?(@closers, character) ->
        :closer

      true ->
        :other
    end
  end

  defp is_matching_closer?(last_opener, current_closer),
    do: current_closer === Map.get(@brackets_map, last_opener)
end
