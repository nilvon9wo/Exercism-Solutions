defmodule Hexadecimal do
  @doc """
    Accept a string representing a hexadecimal value and returns the
    corresponding decimal value.
    It returns the integer 0 if the hexadecimal is invalid.
    Otherwise returns an integer representing the decimal value.

    ## Examples

      iex> Hexadecimal.to_decimal("invalid")
      0

      iex> Hexadecimal.to_decimal("af")
      175

  """
  @ascii_offset ?a - 10

  @spec to_decimal(binary) :: integer
  def to_decimal(hex)
      when is_binary(hex),
      do:
        hex
        |> String.downcase()
        |> String.graphemes()
        |> Enum.reverse()
        |> Enum.with_index()
        |> Enum.map(&get_status_and_value/1)
        |> Enum.unzip()
        |> calculate()

  defp get_status_and_value({character, index}),
    do:
      character
      |> translate_value()
      |> get_status_and_value(index)

  defp get_status_and_value({:ok, value}, index),
    do: {:ok, :math.pow(16, index) * value}

  defp get_status_and_value(result, index),
    do: result

  defp translate_value(character) do
    value =
      case Integer.parse(character) do
        {number, ""} -> number
        _ -> get_letter_value(character) - @ascii_offset
      end

    {check_result(value), value}
  end

  defp get_letter_value(character),
    do:
      character
      |> String.to_charlist()
      |> hd()

  defp check_result(value) do
    if value >= 0 && value <= 15,
      do: :ok,
      else: :error
  end

  defp calculate({statuses, values}) do
    if Enum.all?(statuses, &(&1 === :ok)),
      do: Enum.sum(values),
      else: 0
  end
end
