defmodule PascalsTriangle do
  @doc """
  Calculates the rows of a pascal triangle
  with the given height
  """
  @spec rows(integer) :: [[integer]]
  def rows(1),
    do: [[1]]

  def rows(height),
    do: rows(height, _accumulator = [[1, 1] | rows(1)])

  defp rows(height, accumulator)
       when height === length(accumulator),
       do: Enum.reverse(accumulator)

  defp rows(height, [bottom_row | _] = accumulator),
    do: rows(height, [make_next_row(bottom_row) | accumulator])

  defp make_next_row(previous_row),
    do: make_next_row(previous_row, _accumulator = [1])

  defp make_next_row([penultimate, 1] = _previous_row, accumulator),
    do: [1 | [penultimate + 1 | accumulator]]

  defp make_next_row([head | [second | tail]] = _previous_row, accumulator),
    do: make_next_row([second | tail], [head + second | accumulator])
end
