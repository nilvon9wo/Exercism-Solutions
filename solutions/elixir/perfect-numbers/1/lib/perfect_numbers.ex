defmodule PerfectNumbers do
  @doc """
  Determine the aliquot sum of the given `number`, by summing all the factors
  of `number`, aside from `number` itself.

  Based on this sum, classify the number as:

  :perfect if the aliquot sum is equal to `number`
  :abundant if the aliquot sum is greater than `number`
  :deficient if the aliquot sum is less than `number`
  """
  @spec classify(number :: integer) :: {:ok, atom} | {:error, String.t()}
  def classify(number)
      when number < 1,
      do: {:error, "Classification is only possible for natural numbers."}

  def classify(number)
      when number == 1,
      do: {:ok, :deficient}

  def classify(number),
    do: find_number_type(number)

  defp find_number_type(number),
    do:
      number
      |> find_factors()
      |> Enum.sum()
      |> find_number_type(number)

  defp find_number_type(sum, number)
       when sum < number,
       do: {:ok, :deficient}

  defp find_number_type(sum, number)
       when sum == number,
       do: {:ok, :perfect}

  defp find_number_type(sum, number)
       when sum > number,
       do: {:ok, :abundant}

  defp find_factors(number),
    do:
      1..(number - 1)
      |> Enum.filter(&is_factor(number, &1))

  defp is_factor(target, candidate),
    do: rem(target, candidate) === 0
end
