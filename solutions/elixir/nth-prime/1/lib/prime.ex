defmodule Prime do
  @doc """
  Generates the nth prime.
  """
  @first_prime 2

  @spec nth(non_neg_integer) :: non_neg_integer
  def nth(0),
    do: raise("weird case")

  def nth(count),
    do: find_primes(count)

  defp find_primes(count),
    do: find_primes(count, _last_attempt = @first_prime, accumulator = [@first_prime])

  defp find_primes(count, _last_attempt, accumulator)
       when count === length(accumulator),
       do: hd(accumulator)

  defp find_primes(count, 2 = last_attempt, accumulator),
    do: find_primes(count, _last_attempt = 3, [3 | accumulator])

  defp find_primes(count, last_attempt, accumulator) do
    this_attempt = last_attempt + 2

    accumulator =
      if has_no_factors?(this_attempt),
        do: [this_attempt | accumulator],
        else: accumulator

    find_primes(count, _last_attempt = this_attempt, accumulator)
  end

  defp has_no_factors?(number),
    do: !has_factors?(number)

  defp has_factors?(number),
    do:
      3
      |> Stream.iterate(&(&1 + 2))
      |> Enum.take_while(&(&1 < number - 1))
      |> Enum.any?(&factor?(number, &1))

  defp factor?(number, possible_factor),
    do: rem(number, possible_factor) === 0
end
