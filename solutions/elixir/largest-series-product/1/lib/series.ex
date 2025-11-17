defmodule Series do
  @doc """
  Finds the largest product of a given number of consecutive numbers in a given string of numbers.
  """
  @spec largest_product(String.t(), non_neg_integer) :: non_neg_integer
  def largest_product(_number_string, 0),
    do: 1

  def largest_product(nil = _number_string, _size),
    do: :error

  def largest_product("" = _number_string, _size),
    do: raise(ArgumentError)

  def largest_product(number_string, size)
      when size < 0,
      do: raise(ArgumentError)

  def largest_product(number_string, size) do
    if all_characters_are_digits(number_string) &&
         String.length(number_string) >= size,
       do: find_max_product(number_string, size),
       else: raise(ArgumentError)
  end

  defp all_characters_are_digits(number_string),
    do:
      "^[+-]?[0-9]*\.?[0-9]*$"
      |> Regex.compile!()
      |> Regex.match?(number_string)

  defp find_max_product(number_string, size),
    do:
      find_products(number_string, size)
      |> Enum.max()

  defp find_products(number_string, size),
    do:
      0..(String.length(number_string) - size)
      |> Enum.map(&extract_subseries(number_string, size, &1))
      |> Enum.map(&pair_to_product/1)

  defp extract_subseries(number_string, size, start),
    do: String.slice(number_string, start, size)

  defp pair_to_product(substring),
    do:
      substring
      |> String.graphemes()
      |> Enum.map(&Integer.parse/1)
      |> Enum.map(&elem(&1, 0))
      |> IO.inspect(label: '### pair')
      |> product()

  defp product(factors),
    do: product(factors, _accumulator = 1)

  defp product([] = _factors, accumulator),
    do: accumulator

  defp product([head | tail] = _factors, accumulator),
    do: product(tail, head * accumulator)
end
