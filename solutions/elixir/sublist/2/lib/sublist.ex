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
         |> Enum.map(&Enum.split(big_list, &1))
         |> Enum.map(&elem(&1, 1))
         |> Enum.map(&drop_tails(little_list_tail, &1))
         |> Enum.any?(&(&1 === little_list))

  defp drop_tails(little_list_tail, ending),
    do:
      ending
      |> find_indexes(List.last(little_list_tail))
      |> drop_extra_elements(ending)

  defp drop_extra_elements(last_indexes, ending)
       when length(last_indexes) < 1,
       do: []

  defp drop_extra_elements(last_indexes, ending),
    do:
      ending
      |> Enum.split(List.last(last_indexes) + 1)
      |> elem(0)

  def find_indexes(collection, target_value),
    do:
      collection
      |> Enum.with_index()
      |> Enum.filter(&find_element_with_target_value(target_value, &1))
      |> Enum.map(&elem(&1, 1))

  defp find_element_with_target_value(target_value, {current_value, _index}),
    do: target_value == current_value
end
