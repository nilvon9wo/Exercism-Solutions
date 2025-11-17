defmodule ArmstrongNumber do
  @moduledoc """
  Provides a way to validate whether or not a number is an Armstrong number
  """

  @spec valid?(integer) :: boolean
  def valid?(number),
    do: number == sum_of_digits_to_power_of_length(number)

  defp sum_of_digits_to_power_of_length(number)
       when is_number(number),
       do: sum_of_digits_to_power_of_length(to_string(number))

  defp sum_of_digits_to_power_of_length(number_string),
    do:
      number_string
      |> String.graphemes()
      |> Enum.map(&digit_to_power(&1, String.length(number_string)))
      |> Enum.sum()

  defp digit_to_power(digit_string, power),
    do:
      digit_string
      |> Integer.parse()
      |> elem(0)
      |> :math.pow(power)
end
