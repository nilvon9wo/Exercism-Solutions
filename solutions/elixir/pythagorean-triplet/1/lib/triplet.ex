defmodule Triplet do
  @doc """
  Calculates sum of a given triplet of integers.
  """
  @spec sum([non_neg_integer]) :: non_neg_integer
  def sum([a, b, c] = _triplet),
    do: a + b + c

  @doc """
  Calculates product of a given triplet of integers.
  """
  @spec product([non_neg_integer]) :: non_neg_integer
  def product([a, b, c] = _triplet),
    do: a * b * c

  @doc """
  Determines if a given triplet is pythagorean. That is, do the squares of a and b add up to the square of c?
  """
  @spec pythagorean?([non_neg_integer]) :: boolean
  def pythagorean?([a, b, c]),
    do: square(a) + square(b) == square(c)

  @doc """
  Generates a list of pythagorean triplets from a given min (or 1 if no min) to a given max.
  """
  @spec generate(non_neg_integer, non_neg_integer) :: [list(non_neg_integer)]
  def generate(min \\ 1, max),
    do: generate_triplets(min, max, _accumulator = [])

  def generate_triplets(min, max, accumulator)
      when min > max,
      do: Enum.reverse(accumulator)

  def generate_triplets(min, max, accumulator) do
    triplets =
      min..max
      |> Enum.map(&calculate_distance(min, &1))
      |> Enum.reject(&distance_not_whole_number/1)
      |> Enum.reject(&distance_too_great(&1, max))
      |> Enum.map(&truncate_c/1)

    generate_triplets(min + 1, max, triplets ++ accumulator)
  end

  defp calculate_distance(a, b),
    do: [a, b, :math.sqrt(square(a) + square(b))]

  defp distance_not_whole_number([_a, _b, c]),
    do: c != Kernel.trunc(c)

  defp distance_too_great([_a, _b, c], max),
    do: c > max

  defp truncate_c([a, b, c]),
    do: [a, b, Kernel.trunc(c)]

  @doc """
  Generates a list of pythagorean triplets from a given min to a given max, whose values add up to a given sum.
  """
  @spec generate(non_neg_integer, non_neg_integer, non_neg_integer) :: [list(non_neg_integer)]
  def generate(min, max, sum),
    do:
      min
      |> generate(max)
      |> Enum.reject(&(sum(&1) != sum))

  defp square(value),
    do: :math.pow(value, 2)
end
