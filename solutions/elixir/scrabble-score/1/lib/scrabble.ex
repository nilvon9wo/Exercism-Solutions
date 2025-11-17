defmodule Scrabble do
  @doc """
  Calculate the scrabble score for the word.
  """
  @points_by_values %{
    ["A", "E", "I", "O", "U", "L", "N", "R", "S", "T"] => 1,
    ["D", "G"] => 2,
    ["B", "C", "M", "P"] => 3,
    ["F", "H", "V", "W", "Y"] => 4,
    ["K"] => 5,
    ["J", "X"] => 8,
    ["Q", "Z"] => 10
  }

  @spec score(String.t()) :: non_neg_integer
  def score(word),
    do:
      word
      |> String.upcase()
      |> String.split("")
      |> Enum.map(&convert_letter_to_points/1)
      |> Enum.sum()

  def convert_letter_to_points(letter),
    do:
      @points_by_values
      |> Enum.map(&evaluate(&1, letter))
      |> Enum.sum()

  def evaluate({values, points}, letter) do
    if Enum.member?(values, letter),
      do: points,
      else: 0
  end
end
