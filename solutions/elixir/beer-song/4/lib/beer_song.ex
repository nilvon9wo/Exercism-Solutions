defmodule BeerSong do
  @doc """
  Get a single verse of the beer song
  """
  @starting_count 99
  @penultimate_bottle 2
  @final_bottle 1
  @no_more_bottles 0
  @replace_bottles -1

  @spec verse(integer) :: String.t()
  def verse(@no_more_bottles),
    do:
      basic_verse(@no_more_bottles)
      |> String.replace("Take one down and pass it around,", "Go to the store and buy some more,")
      |> declare_no_bottles()

  def verse(@final_bottle),
    do:
      basic_verse(@final_bottle)
      |> String.replace("Take one", "Take it")
      |> singularize_last_bottle()
      |> declare_no_bottles()

  def verse(@penultimate_bottle),
    do:
      basic_verse(@penultimate_bottle)
      |> singularize_last_bottle()

  def verse(integer),
    do: basic_verse(integer)

  defp basic_verse(integer),
    do:
      """
      #{integer} bottles of beer on the wall, #{integer} bottles of beer.
      Take one down and pass it around, #{calculateAfterPassing(integer)} bottles of beer on the wall.
      """

  @doc """
  Get the entire beer song for a given range of numbers of bottles.
  """
  @spec lyrics(Range.t()) :: String.t()
  def lyrics(range \\ @starting_count..0),
    do:
      range
      |> Enum.map(&verse/1)
      |> Enum.join("\n")

  defp singularize_last_bottle(verse),
    do:
      verse
      |> String.replace(~r/^1 bottles|\s1 bottles/, " 1 bottle")
      |> String.replace(~r/^\s/, "")

  defp declare_no_bottles(verse),
    do:
      verse
      |> String.replace(~r/^0 bottles|\s0 bottles/, " no more bottles")
      |> String.replace(~r/^\sno/, "No")

  defp calculateAfterPassing(@no_more_bottles),
    do: @starting_count

  defp calculateAfterPassing(integer),
    do: integer - 1
end
