defmodule Anagram do
  @doc """
  Returns all candidates that are anagrams of, but not equal to, 'base'.
  """
  @spec match(String.t(), [String.t()]) :: [String.t()]
  def match(base, candidates),
    do:
      base
      |> count
      |> elem(1)
      |> match(base, candidates)

  def match(base_letter_map, base, candidates),
    do:
      candidates
      |> Enum.reject(&(String.upcase(&1) === String.upcase(base)))
      |> Enum.map(&count(&1))
      |> Enum.reject(&has_wrong_letter_count?(&1, base_letter_map))
      |> Enum.map(&elem(&1, 0))

  defp count(word) do
    count_map =
      word
      |> String.upcase()
      |> String.split("")
      |> Enum.reduce(_accumulated = %{}, &update_letter_count/2)

    {word, count_map}
  end

  defp update_letter_count(letter, accumulated),
    do: Map.update(accumulated, letter, 1, &(&1 + 1))

  defp has_wrong_letter_count?({_word, word_map}, base_letter_map),
    do: not Map.equal?(word_map, base_letter_map)
end
