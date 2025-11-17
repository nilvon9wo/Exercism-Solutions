defmodule Wordy do
  @function_by_word %{
    "plus" => &__MODULE__.add/2,
    "minus" => &__MODULE__.subtract/2,
    "multiplied" => &__MODULE__.multiply/2,
    "divided" => &__MODULE__.divide/2
  }

  def add(a, b),
    do: a + b

  def subtract(a, b),
    do: a - b

  def multiply(a, b),
    do: a * b

  def divide(a, b),
    do: div(a, b)

  @doc """
  Calculate the math problem in the sentence.
  """
  @spec answer(String.t()) :: integer
  def answer(question)
      when is_binary(question),
      do:
        question
        |> String.split()
        |> answer()

  def answer(["What", "is" | [first_number, operation, "by", second_number | more]]),
    do: answer(first_number, operation, second_number, more)

  def answer(["What", "is" | [first_number, operation, second_number | more]]),
    do: answer(first_number, operation, second_number, more)

  defp answer(first_number, operation, second_number, more) do
    {first_value, _} = Integer.parse(first_number)
    {second_value, _} = Integer.parse(second_number)

    result = Map.fetch!(@function_by_word, operation).(first_value, second_value)

    if length(more) > 0,
      do: answer(["What", "is", to_string(result) | more]),
      else: result
  end

  def answer(_),
    do: raise(ArgumentError)
end
