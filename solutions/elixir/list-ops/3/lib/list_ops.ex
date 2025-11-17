defmodule ListOps do
  # Please don't use any external modules (especially List or Enum) in your
  # implementation. The point of this exercise is to create these basic
  # functions yourself. You may use basic Kernel functions (like `Kernel.+/2`
  # for adding numbers), but please do not use Kernel functions for Lists like
  # `++`, `--`, `hd`, `tl`, `in`, and `length`.

  @spec count(list) :: non_neg_integer
  def count(list),
    do: count(list, _accumulator = 0)

  defp count([_head | tail] = _list, accumulator),
    do: count(tail, accumulator + 1)

  defp count([] = _list, accumulator),
    do: accumulator

  @spec reverse(list) :: list
  def reverse(list),
    do: reverse(list, _accumulator = [])

  defp reverse([head | tail] = _list, accumulator),
    do: reverse(tail, [head | accumulator])

  defp reverse([] = _list, accumulator),
    do: accumulator

  @spec map(list, (any -> any)) :: list
  def map(list, function),
    do: map(list, _accumulator = [], function)

  defp map([head | tail] = _list, accumulator, function),
    do: map(tail, [function.(head) | accumulator], function)

  defp map([] = _list, accumulator, _function),
    do: reverse(accumulator)

  @spec filter(list, (any -> as_boolean(term))) :: list
  def filter(list, function),
    do: filter(list, _accumulator = [], function)

  defp filter([head | tail] = _list, accumulator, function) do
    if function.(head),
      do: filter(tail, [head | accumulator], function),
      else: filter(tail, accumulator, function)
  end

  defp filter([] = _list, accumulator, _function),
    do: reverse(accumulator)

  @type acc :: any
  @spec reduce(list, acc, (any, acc -> acc)) :: acc
  def reduce([head | tail] = _list, accumulator, function),
    do: reduce(tail, function.(head, accumulator), function)

  def reduce([] = _list, accumulator, _function),
    do: accumulator

  @spec append(list, list) :: list
  def append(list_a, []),
      do: list_a

  def append([], list_b),
      do: list_b

  def append([head | tail], list_b),
      do: [head | append(tail, list_b)]

  @spec concat([[any]]) :: [any]
  def concat([first_list | more_lists] = _list_of_lists),
    do: append(first_list, concat(more_lists))

  def concat([] = _list_of_lists),
    do: []
end
