defmodule Evaluator do
  defstruct [:values, :function_by_words, :inject_into_input]

  @statement_separator ";"

  def new(),
    do: %Evaluator{values: [], function_by_words: %{}}

  def add_functions(%Evaluator{} = evaluator, [] = _forth_modules),
    do: evaluator

  def add_functions(
        %Evaluator{function_by_words: function_by_words} = evaluator,
        [first_module | more_modules]
      ),
      do:
        add_functions(
          %Evaluator{
            evaluator
            | function_by_words:
                Map.merge(function_by_words, first_module.get_function_by_words())
          },
          more_modules
        )

  defp add_function(%Evaluator{} = evaluator, [word | meaning] = _definition) do
    parsed_value = Integer.parse(word)

    if parsed_value == :error,
      do: add_function(evaluator, String.upcase(word), meaning),
      else: raise(Forth.InvalidWord)
  end

  defp add_function(
         %Evaluator{function_by_words: function_by_words} = evaluator,
         word,
         meaning
       ),
       do: %Evaluator{
         evaluator
         | function_by_words:
             Map.update(function_by_words, String.upcase(word), meaning, fn _ ->
               meaning
             end)
       }

  def evaluate_statements(%Evaluator{} = evaluator, input_string)
      when is_binary(input_string) do
    statements =
      input_string
      |> String.split(@statement_separator)
      |> Enum.reject(&(&1 == ""))

    evaluate_statements(evaluator, statements)
  end

  def evaluate_statements(%Evaluator{} = evaluator, []),
    do: evaluator

  def evaluate_statements(%Evaluator{} = evaluator, [first_statement | more_statements]),
    do:
      evaluator
      |> evaluate_tokens(split_tokens(first_statement))
      |> evaluate_statements(more_statements)

  defp split_tokens(first_statement),
    do: Regex.split(~r/(*UTF)(*UCP)[^\w|[:graph:]|[:punct:]]+/, String.trim(first_statement))

  defp evaluate_tokens(%Evaluator{} = evaluator, []),
    do: evaluator

  defp evaluate_tokens(%Evaluator{} = evaluator, [":" | new_function]),
    do: add_function(evaluator, new_function)

  defp evaluate_tokens(%Evaluator{} = evaluator, [first_value | more_values]) do
    parsed_value = Integer.parse(first_value)

    if parsed_value == :error,
      do: evaluate_tokens(evaluator, String.upcase(first_value), more_values),
      else: evaluate_tokens(evaluator, elem(parsed_value, 0), more_values)
  end

  defp evaluate_tokens(%Evaluator{} = evaluator, first_value, more_values)
       when is_number(first_value),
       do: evaluate_tokens(Evaluator.push(evaluator, first_value), more_values)

  defp evaluate_tokens(%Evaluator{} = evaluator, word, more_values) do
    updated_evaluator = Evaluator.apply(evaluator, word)

    if updated_evaluator.inject_into_input,
      do: inject_and_evaluate(updated_evaluator, more_values),
      else: evaluate_tokens(updated_evaluator, more_values)
  end

  defp inject_and_evaluate(
         %Evaluator{inject_into_input: inject_into_input} = evaluator,
         more_values
       ),
       do:
         evaluate_tokens(
           %Evaluator{evaluator | inject_into_input: nil},
           inject_into_input ++ more_values
         )

  def push(%Evaluator{values: values} = evaluator, new_value),
    do: %Evaluator{evaluator | values: [new_value | values]}

  def pop(%Evaluator{values: []} = evaluator),
    do: {nil, evaluator}

  def pop(%Evaluator{values: [head | tail]} = evaluator),
    do: {head, %Evaluator{evaluator | values: tail}}

  def apply(%Evaluator{} = evaluator, word)
      when is_binary(word) do
    definition = select_function(evaluator, word)

    cond do
      is_function(definition) ->
        definition.(evaluator)

      is_list(definition) ->
        %Evaluator{evaluator | inject_into_input: definition}
    end
  end

  defp select_function(%Evaluator{function_by_words: function_by_words}, word) do
    try do
      Map.fetch!(function_by_words, String.upcase(word))
    rescue
      KeyError ->
        raise Forth.UnknownWord
    end
  end
end

defmodule Math do
  def get_function_by_words(),
    do: %{
      "+" => &add/1,
      "-" => &subtract/1,
      "*" => &multiply/1,
      "/" => &divide/1
    }

  def add(%Evaluator{} = evaluator),
    do: Math.apply(evaluator, fn a, b -> a + b end)

  def subtract(%Evaluator{} = evaluator),
    do: Math.apply(evaluator, fn a, b -> a - b end)

  def multiply(%Evaluator{} = evaluator),
    do: Math.apply(evaluator, fn a, b -> a * b end)

  def divide(%Evaluator{} = evaluator),
    do: Math.apply(evaluator, fn a, b -> divide(a, b) end)

  defp divide(_a, 0),
    do: raise(Forth.DivisionByZero)

  defp divide(a, b),
    do: div(a, b)

  def apply(%Evaluator{} = evaluator, function) do
    {value_1, evaluator} = Evaluator.pop(evaluator)
    {value_2, evaluator} = Evaluator.pop(evaluator)
    Evaluator.push(evaluator, function.(value_2, value_1))
  end
end

defmodule StackManipulator do
  def get_function_by_words(),
    do: %{
      "DUP" => &duplicate/1,
      "DROP" => &drop/1,
      "SWAP" => &swap/1,
      "OVER" => &duplicate_penultimate/1
    }

  def duplicate(%Evaluator{values: []}),
    do: raise(Forth.StackUnderflow)

  def duplicate(%Evaluator{values: [first_value | _]} = evaluator),
    do: Evaluator.push(evaluator, first_value)

  def drop(%Evaluator{values: []}),
    do: raise(Forth.StackUnderflow)

  def drop(%Evaluator{} = evaluator),
    do:
      evaluator
      |> Evaluator.pop()
      |> elem(1)

  def swap(%Evaluator{values: values})
      when length(values) < 2,
      do: raise(Forth.StackUnderflow)

  def swap(%Evaluator{} = evaluator) do
    {value_1, evaluator} = Evaluator.pop(evaluator)
    {value_2, evaluator} = Evaluator.pop(evaluator)

    evaluator
    |> Evaluator.push(value_1)
    |> Evaluator.push(value_2)
  end

  def duplicate_penultimate(%Evaluator{values: values})
      when length(values) < 2,
      do: raise(Forth.StackUnderflow)

  def duplicate_penultimate(%Evaluator{values: [_head_1 | [head_2 | _tail]]} = evaluator),
    do: Evaluator.push(evaluator, head_2)
end

defmodule Forth do
  @opaque evaluator :: Evaluator.T

  @doc """
  Create a new evaluator.
  """
  @spec new() :: evaluator
  def new(),
    do:
      Evaluator.new()
      |> Evaluator.add_functions([StackManipulator, Math])

  @doc """
  Evaluate an input string, updating the evaluator state.
  """
  @spec eval(evaluator, String.t()) :: evaluator
  defdelegate eval(evaluator, input_string),
    to: Evaluator,
    as: :evaluate_statements

  @doc """
  Return the current stack as a string with the element on top of the stack
  being the rightmost element in the string.
  """
  @spec format_stack(evaluator) :: String.t()
  def format_stack(%Evaluator{values: values}),
    do:
      values
      |> Enum.reverse()
      |> Enum.join(" ")

  defmodule StackUnderflow do
    defexception []
    def message(_), do: "stack underflow"
  end

  defmodule InvalidWord do
    defexception word: nil
    def message(e), do: "invalid word: #{inspect(e.word)}"
  end

  defmodule UnknownWord do
    defexception word: nil
    def message(e), do: "unknown word: #{inspect(e.word)}"
  end

  defmodule DivisionByZero do
    defexception []
    def message(_), do: "division by zero"
  end
end
