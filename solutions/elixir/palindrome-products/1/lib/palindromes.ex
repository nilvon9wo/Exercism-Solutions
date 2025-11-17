defmodule Palindromes do
  @doc """
  Generates all palindrome products from an optionally given min factor (or 1) to a given max factor.
  """
  @spec generate(non_neg_integer, non_neg_integer) :: map
  def generate(maximum_factor, minimum_factor \\ 1),
    do: generate_for(minimum_factor..maximum_factor)

  defp generate_for(range),
    do:
      range
      |> Enum.map(&get_all_possible_products(range, &1))
      |> Enum.map(&to_palindrome_map/1)
      |> flatten_palindrome_maps()

  defp get_all_possible_products(range, factor),
    do: Enum.map(range, &get_products(factor, &1))

  defp get_products(factor_1, factor_2),
    do: {factor_1 * factor_2, [Enum.sort([factor_1, factor_2])]}

  defp to_palindrome_map(sublist),
    do:
      sublist
      |> Enum.filter(&is_palindrome/1)
      |> Map.new()

  defp flatten_palindrome_maps([first_map | more_maps]),
    do: flatten_palindrome_maps(more_maps, first_map)

  defp flatten_palindrome_maps([], accumulator),
    do: accumulator

  defp flatten_palindrome_maps([first_map | more_maps], accumulator),
    do: flatten_palindrome_maps(more_maps, Map.merge(first_map, accumulator, &merge_maps/3))

  defp merge_maps(_key, [factors_to_add], factors_in_accumulator) do
    if Enum.any?(factors_in_accumulator, &(&1 === factors_to_add)),
      do: factors_in_accumulator,
      else: [factors_to_add | factors_in_accumulator]
  end

  defp is_palindrome({value, _}),
    do: value === reverse_digits(value)

  defp reverse_digits(value),
    do:
      value
      |> to_string()
      |> String.graphemes()
      |> Enum.reverse()
      |> Enum.join()
      |> Integer.parse()
      |> elem(0)
end
