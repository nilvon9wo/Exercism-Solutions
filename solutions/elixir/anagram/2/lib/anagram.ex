defmodule Anagram do
  @doc """
  Returns all candidates that are anagrams of, but not equal to, 'base'.
  """
  @spec match(String.t(), [String.t()]) :: [String.t()]
  def match(base, candidates),
    do: candidates
        |> Enum.reject(&(String.upcase(&1) === String.upcase(base)))
        |> Enum.reject(&(graph(&1) != graph(base)))

  defp graph(word),
    do: word
    |> String.upcase()
    |> String.graphemes()
    |> Enum.sort()
end
