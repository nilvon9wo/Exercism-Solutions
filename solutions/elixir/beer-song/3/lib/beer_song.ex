defmodule BeerSong do
  @doc """
  Get a single verse of the beer song
  """

  @spec verse(integer) :: String.t()
  def verse(integer)
       when integer === 0,
       do: """
       No more bottles of beer on the wall, no more bottles of beer.
       Go to the store and buy some more, 99 bottles of beer on the wall.
       """

  def verse(integer)
      when integer === 1,
      do: """
      1 bottle of beer on the wall, 1 bottle of beer.
      Take it down and pass it around, no more bottles of beer on the wall.
      """

  def verse(integer)
      when integer === 2,
      do: """
      2 bottles of beer on the wall, 2 bottles of beer.
      Take one down and pass it around, 1 bottle of beer on the wall.
      """

  def verse(integer),
    do: """
    #{integer} bottles of beer on the wall, #{integer} bottles of beer.
    Take one down and pass it around, #{integer - 1} bottles of beer on the wall.
    """

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
