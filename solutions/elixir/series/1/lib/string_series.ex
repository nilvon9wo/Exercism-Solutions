defmodule StringSeries do
  @doc """
  Given a string `s` and a positive integer `size`, return all substrings
  of that size. If `size` is greater than the length of `s`, or less than 1,
  return an empty list.
  """
  @spec slices(s :: String.t(), size :: integer) :: list(String.t())
  def slices(_string, size)
      when size <= 0,
      do: []

  def slices(string, size) do
    string_length = String.length(string)

    if string_length < size,
      do: [],
      else: slices(string, size, 0..(string_length - size))
  end

  defp slices(string, size, slice_range),
    do: Enum.map(slice_range, &String.slice(string, &1, size))
end
