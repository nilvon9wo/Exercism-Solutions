defmodule AllYourBase do
  @doc """
  Given a number in base a, represented as a sequence of digits, converts it to base b,
  or returns nil if either of the bases are less than 2
  """

  @spec convert(list, integer, integer) :: list
  def convert(digits, original_base, target_base)
      when is_list(digits) and
             length(digits) >= 1 and
             original_base > 1 and
             target_base > 1 do
    if has_invalid_inputs?(digits, original_base),
      do: nil,
      else:
        digits
        |> Enum.drop_while(&(&1 == 0))
        |> to_base_10_value(original_base)
        |> trunc()
        |> to_target_base(target_base)
  end

  def convert(_digits, _original_base, _new_base),
    do: nil

  defp has_invalid_inputs?(digits, original_base),
    do:
      not Enum.all?(digits, &(&1 >= 0)) ||
        Enum.any?(digits, &(&1 >= original_base))

  defp to_base_10_value(original_numbers, original_base),
    do: to_base_10_value(original_numbers, original_base, _power = 0, _accumulated = 0)

  defp to_base_10_value(original_numbers, _original_base, power, accumulated)
       when power === length(original_numbers),
       do: accumulated

  defp to_base_10_value(original_numbers, original_base, power, accumulated),
    do:
      to_base_10_value(
        original_numbers,
        original_base,
        power + 1,
        add_translated_value(original_numbers, original_base, power, accumulated)
      )

  defp add_translated_value(original_numbers, original_base, power, accumulated) do
    next_value_position = length(original_numbers) - power - 1
    original_number = elem(List.pop_at(original_numbers, next_value_position), 0)
    accumulated + original_number * :math.pow(original_base, power)
  end

  defp to_target_base(_base_10_value = 0, _target_base),
    do: [0]

  defp to_target_base(base_10_value, _target_base = 10),
    do:
      base_10_value
      |> Integer.to_string()
      |> String.graphemes()
      |> Enum.map(&String.to_integer/1)

  defp to_target_base(base_10_value, target_base),
    do:
      to_target_base(
        base_10_value,
        target_base,
        highest_power_of_base(base_10_value, target_base),
        _accumulated = []
      )

  defp to_target_base(base_10_value, _target_base, 0, accumulated),
    do: Enum.reverse(accumulated)

  defp to_target_base(base_10_value, target_base, highest_power, accumulated),
    do:
      to_target_base(
        rem(base_10_value, highest_power),
        target_base,
        div(highest_power, target_base),
        [div(base_10_value, highest_power) | accumulated]
      )

  defp highest_power_of_base(base_10_value, target_base, last_attempted \\ 1) do
    next_attempt = trunc(last_attempted * target_base)

    if next_attempt > base_10_value,
      do: last_attempted,
      else: highest_power_of_base(base_10_value, target_base, next_attempt)
  end
end
