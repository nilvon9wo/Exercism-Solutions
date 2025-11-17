defmodule Squares do
  @moduledoc """
  Calculate sum of squares, square of sum, difference between two sums from 1 to a given end number.
  """

  @doc """
  Calculate sum of squares from 1 to a given end number.
  """
  @spec sum_of_squares(pos_integer) :: pos_integer
  def sum_of_squares(number),
    do: sum_of_squares(number, _accumulated = 0)

  defp sum_of_squares(number, accumulated)
       when number > 0,
       do: sum_of_squares(number - 1, accumulated + square(number))

  defp sum_of_squares(_number, accumulated),
    do: accumulated

  @doc """
  Calculate square of sum from 1 to a given end number.
  """
  @spec square_of_sum(pos_integer) :: pos_integer
  def square_of_sum(number),
    do: square_of_sum(number, _accumulated = 0)

  defp square_of_sum(number, accumulated)
       when number > 0,
       do: square_of_sum(number - 1, accumulated + number)

  defp square_of_sum(_number, accumulated),
    do: square(accumulated)

  @doc """
  Calculate difference between sum of squares and square of sum from 1 to a given end number.
  """
  @spec difference(pos_integer) :: pos_integer
  def difference(number),
    do: square_of_sum(number) - sum_of_squares(number)

  defp square(number),
    do: :math.pow(number, 2)
end
