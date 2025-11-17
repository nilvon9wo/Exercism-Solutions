defmodule Words do
  @doc """
  Count the number of words in the sentence.

  Words are compared case-insensitively.
  """
  @spec count(String.t()) :: map
  def count(sentence),
    do:
      sentence
      |> clean()
      |> String.split(" ")
      |> Enum.reject(&(String.trim(&1) === ""))
      |> count_words()

  defp clean(sentence),
    do:
      Regex.replace(~r/[:|!|@|#|$|%|^|&|*|(|)|_|,]/, sentence, " ")
      |> String.downcase()
      |> String.trim()

  defp count_words(words),
    do: count_words(words, _accumulated = %{})

  defp count_words(words, accumulated)
       when length(words) === 0,
       do: accumulated

  defp count_words([head | tail], accumulated) do
    count = Map.get(accumulated, head, 0) + 1
    accumulated = Map.merge(accumulated, %{head => count})
    count_words(tail, accumulated)
  end
end
