defmodule SumOfMultiples do
  @doc """
  Adds up all numbers from 1 to a given end number that are multiples of the factors provided.
  """
  @spec to(non_neg_integer, [non_neg_integer]) :: non_neg_integer
  def to(limit, factors),
      do: factors
          |> Enum.map(&find_multiples(&1, limit))
          |> List.flatten()
          |> Enum.uniq()
          |> Enum.sum()

  defp find_multiples(factor, upper_limit),
       do: Range.new(1, div(upper_limit, factor))
           |> Enum.map(&(&1 * factor))
           |> Enum.reject(&(&1 >= upper_limit))
end
