defmodule PrimeFactors do
  @doc """
  Compute the prime factors for 'number'.

  The prime factors are prime numbers that when multiplied give the desired
  number.

  The prime factors of 'number' will be ordered lowest to highest.
  """
  @spec factors_for(pos_integer) :: [pos_integer]
  def factors_for(number)
      when number < 2,
      do: []

  def factors_for(number),
    do: factors_for(number, _next_attempt = 2, _accumulator = [])

  def factors_for(number, next_attempt, accumulator)
      when next_attempt > number,
      do:
        accumulator
        |> Enum.sort()

  def factors_for(number, next_attempt, accumulator) do
    if rem(number, next_attempt) === 0,
      do: factors_for(div(number, next_attempt), next_attempt, [next_attempt | accumulator]),
      else: factors_for(number, next_prime(next_attempt), accumulator)
  end

  defp next_prime(2),
    do: 3

  defp next_prime(n),
    do: n + 2
end
