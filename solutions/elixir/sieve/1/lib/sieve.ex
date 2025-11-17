defmodule Sieve do
  @doc """
  Generates a list of primes up to a given limit.
  """

  @lower_limit 2
  @could_be_prime true
  @not_prime false

  @spec primes_to(non_neg_integer) :: [non_neg_integer]
  def primes_to(upper_limit)
      when upper_limit < @lower_limit,
      do: []

  def primes_to(upper_limit),
    do:
      @lower_limit..upper_limit
      |> create_marked_map(@could_be_prime)
      |> mark_primes(@lower_limit, upper_limit)
      |> filter_for_primes()
      |> Map.keys()
      |> Enum.sort()

  defp create_marked_map(range, is_prime),
    do:
      range
      |> Enum.map(&{&1, is_prime})
      |> Map.new()

  defp mark_primes(marked_map, nil, _upper_limit),
    do: marked_map

  defp mark_primes(old_marked_map, current_value, upper_limit) do
    new_marked_map =
      current_value
      |> create_disqualified_range(upper_limit)
      |> create_marked_map(@not_prime)

    merged_marked_map = Map.merge(old_marked_map, new_marked_map)

    next_value = find_next_possible_prime(merged_marked_map, current_value)
    mark_primes(merged_marked_map, next_value, upper_limit)
  end

  defp create_disqualified_range(current_value, upper_limit) do
    for x <- current_value..upper_limit,
        do: x * current_value
  end

  defp find_next_possible_prime(merged_marked_map, current_value),
    do:
      merged_marked_map
      |> filter_for_primes()
      |> Map.keys()
      |> Enum.filter(&(&1 > current_value))
      |> find_next_possible_prime()

  defp find_next_possible_prime([] = _larger_possible_primes),
    do: nil

  defp find_next_possible_prime(larger_possible_primes),
    do: Enum.min(larger_possible_primes)

  defp filter_for_primes(marked_map),
    do: :maps.filter(fn _, value -> value end, marked_map)
end
