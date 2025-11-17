defmodule Luhn do
  require Integer

  @doc """
  Checks if the given number is valid via the luhn formula
  """
  @spec valid?(String.t()) :: boolean
  def valid?(number) do
    clean_input = String.replace(number, " ", "")

    valid_input?(clean_input) and
      rem(sum_of(clean_input), 10) === 0
  end

  defp valid_input?(clean_input),
    do:
      String.length(clean_input) > 1 and
        Regex.match?(~r/^\d+$/, clean_input)

  defp sum_of(clean_input),
    do:
      clean_input
      |> String.reverse()
      |> String.graphemes()
      |> Enum.with_index()
      |> Enum.map(&double/1)
      |> Enum.map(&reduce_overage/1)
      |> Enum.sum()

  defp double({character, index}),
    do:
      character
      |> String.to_integer()
      |> double(index)

  defp double(value, index)
       when Integer.is_odd(index),
       do: value * 2

  defp double(value, _index),
    do: value

  defp reduce_overage(integer)
       when integer > 9,
       do: integer - 9

  defp reduce_overage(integer),
    do: integer
end
