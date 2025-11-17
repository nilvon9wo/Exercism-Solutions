defmodule Permutation do
  def without_repetitions([], _k),
    do: [[]]

  def without_repetitions(_list, 0),
    do: [[]]

  def without_repetitions(list, k) do
    for head <- list,
        tail <- without_repetitions(list -- [head], k - 1),
        do: [head | tail]
  end
end

defmodule Alphametics do
  @digits [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

  @type puzzle :: binary
  @type solution :: %{required(?A..?Z) => 0..9}

  @doc """
  Takes an alphametics puzzle and returns a solution where every letter
  replaced by its number will make a valid equation. Returns `nil` when
  there is no valid solution to the given puzzle.

  ## Examples

      iex> Alphametics.solve("I + BB == ILL")
      %{?I => 1, ?B => 9, ?L => 0}

      iex> Alphametics.solve("A == B")
      nil
  """
  @spec solve(puzzle) :: solution | nil
  def solve(puzzle) do
    letters = extract_letters(puzzle)

    solutions =
      solve(puzzle, letters)

    if length(solutions) == 1,
      do: change_keys_to_characters(List.first(solutions)),
      else: nil
  end

  defp solve(puzzle, letters) do
    letter_count = length(letters)
    not_zero_letters = rule_out_zeros(puzzle)

    @digits
    |> Permutation.without_repetitions(letter_count)
    |> Enum.map(&List.zip([letters, &1]))
    |> Enum.reject(&has_illegal_zero(&1, not_zero_letters))
    |> Enum.filter(&evaluate(puzzle, &1))
  end

  defp extract_letters(puzzle),
    do:
      puzzle
      |> String.replace(~r/[\W]+/, "")
      |> String.graphemes()
      |> Enum.uniq()

  defp rule_out_zeros(puzzle),
    do:
      puzzle
      |> extract_words()
      |> rule_out_zeros(_accumulator = [])

  defp rule_out_zeros([] = _words, accumulator),
    do: accumulator

  defp rule_out_zeros([first_word | more_words], accumulator),
    do: rule_out_zeros(more_words, [String.first(first_word) | accumulator])

  defp extract_words(puzzle) do
    [addend_string, sum] = String.split(puzzle, " == ")
    addends = String.split(addend_string, " + ")
    [sum | addends]
  end

  defp has_illegal_zero([], _not_zero_letters),
    do: false

  defp has_illegal_zero([{letter, 0} | more_pairs], not_zero_letters),
    do:
      Enum.member?(not_zero_letters, letter) &&
        !has_illegal_zero(more_pairs, not_zero_letters)

  defp has_illegal_zero([{_letter, _value} | more_pairs], not_zero_letters),
    do: has_illegal_zero(more_pairs, not_zero_letters)

  defp evaluate(puzzle, value_by_letter_map),
    do:
      puzzle
      |> replace_letters(value_by_letter_map)
      |> Code.eval_string()
      |> elem(0)

  defp replace_letters(puzzle, %{} = value_by_letter_map),
    do: replace_letters(puzzle, Map.to_list(value_by_letter_map))

  defp replace_letters(puzzle, [] = _letter_value_pairs),
    do: puzzle

  defp replace_letters(puzzle, [{letter, value} | more_pairs] = _letter_value_pairs),
    do: replace_letters(String.replace(puzzle, letter, to_string(value)), more_pairs)

  defp change_keys_to_characters(value_by_letter_map) do
    value_by_character_pairs =
      for {letter, value} <- value_by_letter_map,
          do: {hd(String.to_charlist(letter)), value}

    Map.new(value_by_character_pairs)
  end
end
