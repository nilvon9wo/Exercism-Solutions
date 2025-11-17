defmodule Sublist do
  @doc """
  Returns whether the first list is a sublist or a superlist of the second list
  and if not whether it is equal or unequal to the second list.

  List A is a sublist of list B if by dropping 0 or more elements from the front of B
  and 0 or more elements from the back of B you get a list that's completely equal to A.
  """
  def compare(a, b)
      when length(a) === 0 and
             length(b) === 0,
      do: :equal

  def compare(a, _b)
      when length(a) === 0,
      do: :sublist

  def compare(_a, b)
      when length(b) === 0,
      do: :superlist

  def compare(a, b)
      when length(a) < length(b) do
    if is_first_sublist_of_second?(a, b),
      do: :sublist,
      else: :unequal
  end

  def compare(a, b)
      when length(a) === length(b) do
    if a === b,
      do: :equal,
      else: :unequal
  end

  def compare(a, b)
      when length(a) > length(b) do
    if is_first_sublist_of_second?(b, a),
      do: :superlist,
      else: :unequal
  end

  def compare(_a, _b),
    do: :unequal

  defp is_first_sublist_of_second?(
         [little_list_head | little_list_tail] = little_list,
         big_list
       ),
       do:
         big_list
         |> find_indexes(little_list_head)
         |> Enum.reject(&refute_inexact(little_list_head, big_list, &1))
         |> Enum.map(&Enum.split(big_list, &1))
         |> Enum.map(fn {_beginning, ending} -> ending end)
         |> Enum.map(&drop_tails(little_list_tail, &1))
         |> Enum.any?(&(&1 === little_list))

  defp drop_tails(little_list_tail, ending) do
    little_list_last = List.last(little_list_tail)
    last_indexes = find_indexes(ending, little_list_last)

    if length(last_indexes) > 0,
      do: drop_tails(little_list_last, ending, List.last(last_indexes)),
      else: drop_tails(little_list_last, ending, _final_last_index = nil)
  end

  defp drop_tails(_little_list_last, _ending, _final_last_index = nil),
    do: []

  defp drop_tails(_little_list_last, ending, final_last_index) do
    {beginning, _} = Enum.split(ending, final_last_index + 1)
    beginning
  end

  defp refute_inexact(little_list_head, big_list, index) do
    {big_list_value, _} = List.pop_at(big_list, index)
    little_list_head !== big_list_value
  end

  def find_indexes(collection, target_value),
    do:
      collection
      |> Enum.with_index()
      |> Enum.filter(&find_element_with_target_value(target_value, &1))
      |> Enum.map(&elem(&1, 1))

  defp find_element_with_target_value(target_value, {current_value, _index}),
    do: target_value == current_value
end
