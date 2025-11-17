defmodule BinarySearch do
  @doc """
    Searches for a key in the tuple using the binary search algorithm.
    It returns :not_found if the key is not in the tuple.
    Otherwise returns {:ok, position}.

    ## Examples

      iex> BinarySearch.search({}, 2)
      :not_found

      iex> BinarySearch.search({1, 3, 5}, 2)
      :not_found

      iex> BinarySearch.search({1, 3, 5}, 5)
      {:ok, 2}

  """

  @spec search(tuple, integer) :: {:ok, integer} | :not_found
  def search(numbers, key)
      when is_tuple(numbers) do
    number_list = Tuple.to_list(numbers)

    if Enum.member?(number_list, key),
      do: search(number_list, key, _positions_checked = []),
      else: :not_found
  end

  def search(numbers, key, positions_checked)
      when length(numbers) === 1 or
             hd(numbers) === key,
      do: {:ok, Enum.sum(positions_checked)}

  def search(numbers, key, old_positions_checked) do
    new_position = round(length(numbers) / 2)
    {head_numbers, tail_numbers} = Enum.split(numbers, new_position)

    if key < hd(tail_numbers),
      do: search(head_numbers, key, old_positions_checked),
      else: search(tail_numbers, key, [new_position | old_positions_checked])
  end
end
