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
        |> String.split(~r/[^[:alnum:]-]/u, trim: true)
        |> count_words()

  defp count_words(words),
       do: Enum.reduce(words, _accumulated = %{}, &update_word_count/2)

  defp update_word_count(word, accumulated),
       do: Map.update(accumulated, word, 1, &(&1 + 1))
end
