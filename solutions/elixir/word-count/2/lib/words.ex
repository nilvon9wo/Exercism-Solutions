defmodule Words do
  @doc """
  Count the number of words in the sentence.

  Words are compared case-insensitively.
  """
  @spec count(String.t()) :: map
  def count(sentence),
    do:
      sentence
      |> String.downcase()
      |> String.split(~r/[\s|:|!|@|#|$|%|^|&|*|(|)|_|,]/, trim: true)
      |> Enum.reject(&(String.trim(&1) === ""))
      |> count_words(_accumulated = %{})

  defp count_words(words, accumulated)
       when length(words) === 0,
       do: accumulated

  defp count_words([head | tail], accumulated),
    do: count_words(tail, Map.update(accumulated, head, 1, &(&1 + 1)))
end
