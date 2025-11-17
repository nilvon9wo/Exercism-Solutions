defmodule Grains do
  @doc """
  Calculate two to the power of the input minus one.
  """
  @spec square(pos_integer) :: pos_integer
  def square(integer)
      when integer < 1 or integer > 64,
      do: {:error, "The requested square must be between 1 and 64 (inclusive)"}

  def square(integer),
    do: {:ok, round(:math.pow(2, integer - 1))}

  @doc """
  Adds square of each number from 1 to 64.
  """
  @spec total :: pos_integer
  def total() do
    total =
      1..64
      |> Enum.map(&square/1)
      |> Enum.reduce(_accumulator = 0, &add_grain/2)

    {:ok, total}
  end

  defp add_grain(grain, accumulator) do
    {:ok, grain_count} = grain
    accumulator + grain_count
  end
end
