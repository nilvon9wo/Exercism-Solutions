defmodule CollatzConjecture do
  @doc """
  calc/1 takes an integer and returns the number of steps required to get the
  number to 1 when following the rules:
    - if number is odd, multiply with 3 and add 1
    - if number is even, divide by 2
  """
  defguard is_even(value)
           when rem(value, 2) == 0

  defguard is_odd(value)
           when rem(value, 2) == 1

  @spec calc(input :: pos_integer()) :: non_neg_integer()
  def calc(input)
      when input <= 0,
      do: raise(FunctionClauseError)

  def calc(1),
    do: 0

  def calc(input)
      when is_odd(input),
      do: calc(3 * input + 1) + 1

  def calc(input)
      when is_even(input),
      do: calc(div(input, 2)) + 1
end
