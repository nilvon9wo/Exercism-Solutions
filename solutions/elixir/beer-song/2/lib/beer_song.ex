defmodule BeerSong do
  @doc """
  Get a single verse of the beer song
  """
  @no_bottles "no more bottles"
  @one_bottle "1 bottle"

  @spec verse(integer) :: String.t()
  def verse(number),
    do:
      verse(
        _before_passing = calculate(number),
        _after_passing = calculate(number - 1)
      )

  defp verse(before_passing, after_passing)
       when before_passing === @no_bottles,
       do: """
       No more bottles of beer on the wall, #{before_passing} of beer.
       Go to the store and buy some more, #{after_passing} of beer on the wall.
       """

  defp verse(before_passing, after_passing),
       do: """
       #{before_passing} of beer on the wall, #{before_passing} of beer.
       Take #{take_pronoun(before_passing)} down and pass it around, #{after_passing} of beer on the wall.
       """

  defp calculate(-1),
       do: "99 bottles"

  defp calculate(0),
    do: @no_bottles

  defp calculate(1),
    do: @one_bottle

  defp calculate(number),
    do: "#{number} bottles"

  defp take_pronoun(@one_bottle),
    do: "it"

  defp take_pronoun(_),
       do: "one"


  @doc """
  Get the entire beer song for a given range of numbers of bottles.
  """
  @spec lyrics(Range.t()) :: String.t()
  def lyrics(range \\ 99..0),
    do:
      range
      |> Enum.map(&verse/1)
      |> Enum.join("\n")
end
