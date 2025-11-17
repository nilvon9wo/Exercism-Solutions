defmodule Pangram do
  @doc """
  Determines if a word or sentence is a pangram.
  A pangram is a sentence using every letter of the alphabet at least once.

  Returns a boolean.

    ## Examples

      iex> Pangram.pangram?("the quick brown fox jumps over the lazy dog")
      true

  """
  @alphabet Enum.map(?A..?Z, &<<&1::utf8>>)

  @spec pangram?(String.t()) :: boolean
  def pangram?(sentence),
    do:
      sentence
      |> String.upcase()
      |> String.split("")
      |> contains_all_letters?()

  defp contains_all_letters?(letter_list),
    do: Enum.all?(@alphabet, &Enum.member?(letter_list, &1))
end
