defmodule Isogram do
  @doc """
  Determines if a word or sentence is an isogram
  """
  @alphabet Enum.map(?A..?Z, &<<&1::utf8>>)

  @spec isogram?(String.t()) :: boolean
  def isogram?(sentence),
    do:
      sentence
      |> String.upcase()
      |> String.split("")
      |> Enum.reject(&(!Enum.member?(@alphabet, &1)))
      |> contains_letters_just_once?()

  def contains_letters_just_once?(sentence),
    do: length(sentence) === length(Enum.uniq(sentence))
end
