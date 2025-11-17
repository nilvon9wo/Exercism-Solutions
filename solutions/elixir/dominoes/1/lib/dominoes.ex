defmodule NumberPool do
  defstruct [:members]

  def new({a, b}),
    do: %NumberPool{members: MapSet.new([a, b])}

  def add(%NumberPool{members: members}, {a, b} = _domino),
    do: %NumberPool{
      members:
        members
        |> MapSet.put(a)
        |> MapSet.put(b)
    }

  def add(%NumberPool{members: members_1}, %NumberPool{members: members_2}),
    do: %NumberPool{members: MapSet.union(members_1, members_2)}

  def either?(%NumberPool{members: members}, {a, b}),
    do:
      MapSet.member?(members, a) ||
        MapSet.member?(members, b)

  def disjoint?(%NumberPool{members: members_1}, %NumberPool{members: members_2}),
    do: MapSet.disjoint?(members_1, members_2)
end

defmodule Dominoes do
  @type domino :: {1..6, 1..6}

  @doc """
  chain?/1 takes a list of domino stones and returns boolean indicating if it's
  possible to make a full chain
  """
  @spec chain?(dominoes :: [domino] | []) :: boolean
  def chain?([]),
    do: true

  def chain?([{a, b} = _domino]),
    do: a == b

  def chain?(dominoes) do
    all_even =
      dominoes
      |> count_values()
      |> Map.values()
      |> Enum.all?(&(rem(&1, 2) == 0))

    if all_even,
      do: can_all_be_connected?(dominoes),
      else: false
  end

  def can_all_be_connected?(dominoes),
    do:
      dominoes
      |> create_pools()
      |> reduce()
      |> is_just_one?()

  defp count_values(dominoes),
    do: count_values(dominoes, _occurrence_count_by_value = %{})

  defp count_values([] = _dominoes, occurrence_count_by_value),
    do: occurrence_count_by_value

  defp count_values([{a, b} | tail] = _dominoes, occurrence_count_by_value) do
    occurrence_count_by_value = Map.update(occurrence_count_by_value, a, 1, &(&1 + 1))
    occurrence_count_by_value = Map.update(occurrence_count_by_value, b, 1, &(&1 + 1))
    count_values(tail, occurrence_count_by_value)
  end

  defp create_pools(dominoes),
    do: create_pools(dominoes, _accumulator = [])

  defp create_pools([] = _dominoes, accumulator),
    do: accumulator

  defp create_pools([head | tail], [] = _accumulator),
    do: create_pools(tail, [NumberPool.new(head)])

  defp create_pools([head | tail], accumulator),
    do: create_pools(tail, add_to_pools(head, accumulator))

  defp add_to_pools(domino, [first_pool | more_pools] = _accumulator) do
    if NumberPool.either?(first_pool, domino),
      do: [NumberPool.add(first_pool, domino) | more_pools],
      else:
        add_to_pools(
          domino,
          _rejected_accumulator = [first_pool],
          _untested_accumulator = more_pools
        )
  end

  defp add_to_pools(domino, rejected_accumulator, [] = _untested_accumulator),
    do: [NumberPool.new(domino) | rejected_accumulator]

  defp add_to_pools(
         domino,
         rejected_accumulator,
         [first_pool | more_pools] = untested_accumulator
       ) do
    if NumberPool.either?(first_pool, domino),
      do: [NumberPool.add(first_pool, domino) | more_pools] ++ untested_accumulator,
      else:
        add_to_pools(
          domino,
          [first_pool | rejected_accumulator],
          _untested_accumulator = more_pools
        )
  end

  defp reduce(pools)
       when length(pools) == 1,
       do: pools

  defp reduce(pools) do
    merged_pools = merge(pools)

    cond do
      length(merged_pools) == 1 ->
        merged_pools

      length(merged_pools) == length(pools) ->
        merged_pools

      true ->
        reduce(merged_pools)
    end
  end

  defp merge([first_pool]),
    do: [first_pool]

  defp merge([first_pool, second_pool | more_pools]) do
    if !NumberPool.disjoint?(first_pool, second_pool),
      do: merge([NumberPool.add(first_pool, second_pool) | more_pools]),
      else: merge(first_pool, _rejected_pools = [second_pool], _other_pools = more_pools)
  end

  defp merge(first_pool, rejected_pools, [] = _other_pools),
    do: [first_pool | rejected_pools]

  defp merge(first_pool, rejected_pools, [second_pool | more_pools]) do
    if !NumberPool.disjoint?(first_pool, second_pool),
      do: merge([NumberPool.add(first_pool, second_pool), rejected_pools, more_pools]),
      else: merge(first_pool, [second_pool | rejected_pools], more_pools)
  end

  defp is_just_one?(pools),
    do: length(pools) == 1
end
