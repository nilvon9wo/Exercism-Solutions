defmodule CollatzConjecture do
  @doc """
  calc/1 takes an integer and returns the number of steps required to get the
  number to 1 when following the rules:
    - if number is odd, multiply with 3 and add 1
    - if number is even, divide by 2
  """
  @spec calc(input :: pos_integer()) :: non_neg_integer()
  def calc(input)
      when input <= 0 or
             is_binary(input),
      do: raise(FunctionClauseError)

  def calc(input),
    do: calc(input, _step_count = 0)

  def calc(input, step_count)
      when input === 1,
      do: step_count

  def calc(input, step_count) do
    if is_even?(input),
      do: calc(:even, input, step_count),
      else: calc(:odd, input, step_count)
  end

  def calc(:even, input, step_count),
    do: calc(div(input, 2), step_count + 1)

  def calc(:odd, input, step_count),
    do: calc(3 * input + 1, step_count + 1)

  defp is_even?(input),
    do: rem(input, 2) == 0
end
