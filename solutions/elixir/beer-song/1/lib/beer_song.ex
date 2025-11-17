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

  defp verse(before_passing = @no_bottles, _after_passing),
    do:
      verse(
        before_passing,
        _after_passing = calculate(99),
        _directive = "Go to the store and buy some more, "
      )

  defp verse(before_passing, after_passing),
    do: verse(before_passing, after_passing, _directive = order_take(before_passing))

  defp verse(before_passing, after_passing, directive),
    do: "#{recite_count(before_passing)}#{directive}#{recite_remaining(after_passing)}"

  defp recite_count(before_passing),
    do: "#{proper_case(before_passing)} of beer on the wall, #{before_passing} of beer.\n"

  defp recite_remaining(after_passing),
    do: "#{after_passing} of beer on the wall.\n"

  defp order_take(@one_bottle),
    do: take("it")

  defp order_take(_),
    do: take("one")

  defp take(what),
    do: "Take #{what} down and pass it around, "

  defp proper_case(string),
    do: String.replace(string, "n", "N")

  defp calculate(0),
    do: @no_bottles

  defp calculate(1),
    do: @one_bottle

  defp calculate(number),
    do: "#{number} bottles"

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
