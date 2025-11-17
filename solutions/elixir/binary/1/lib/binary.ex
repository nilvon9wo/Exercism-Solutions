defmodule Binary do
  @doc """
  Convert a string containing a binary number to an integer.

  On errors returns 0.
  """
  @binary_characters ["0", "1"]

  @spec to_decimal(String.t()) :: non_neg_integer
  def to_decimal(string) do
    graphemes = String.graphemes(string)

    if all_binary?(graphemes),
      do: convert(graphemes),
      else: 0
  end

  def all_binary?(graphemes),
    do: Enum.all?(graphemes, &Enum.member?(@binary_characters, &1))

  defp convert(string),
    do:
      string
      |> Enum.reverse()
      |> Enum.with_index()
      |> Enum.map(&power_of_index/1)
      |> Enum.sum()

  defp power_of_index({"0", _index}),
    do: 0

  defp power_of_index({"1", index}),
    do: :math.pow(2, index)
end
