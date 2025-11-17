defmodule Change do
  @doc """
    Determine the least number of coins to be given to the user such
    that the sum of the coins' value would equal the correct amount of change.
    It returns {:error, "cannot change"} if it is not possible to compute the
    right amount of coins. Otherwise returns the tuple {:ok, list_of_coins}

    ## Examples

      iex> Change.generate([5, 10, 15], 3)
      {:error, "cannot change"}

      iex> Change.generate([1, 5, 10], 18)
      {:ok, [1, 1, 1, 5, 10]}

  """
  @error {:error, "cannot change"}

  @spec generate(list, integer) :: {:ok, list} | {:error, String.t()}
  def generate(_coins, target)
      when target === 0,
      do: {:ok, []}

  def generate([smallest_coin | _more_coins], target)
      when target < smallest_coin,
      do: @error

  def generate(coins, target),
    do:
      coins
      |> Enum.reverse()
      |> generate(target, _accumulator = [])

  def generate(coins, target, _accumulator)
      when length(coins) === 0 and
             target !== 0,
      do: @error

  def generate(coins, _target, accumulator)
      when length(coins) === 0,
      do: {:ok, accumulator}

  def generate([largest_coin | more_coins], target, accumulator)
      when largest_coin > target,
      do: generate(more_coins, target, accumulator)

  def generate([largest_coin | _more_coins] = coins, target, accumulator) do
    obvious_result =
      {obvious_status, obvious_collection} =
      generate(coins, target - largest_coin, [largest_coin | accumulator])

    if obvious_status === :ok && length(obvious_collection) === 1,
      do: {:ok, obvious_collection},
      else: generate_alternatives(coins, target, accumulator, obvious_result)
  end

  def generate_alternatives([_largest_coin | more_coins], target, accumulator, {:error, _}),
    do: generate(more_coins, target, accumulator)

  def generate_alternatives(
        [largest_coin | more_coins],
        target,
        accumulator,
        {:ok, obvious_collection}
      ) do
    {alternative_status, alternative_collection} =
      more_coins
      |> Enum.drop_while(&(rem(largest_coin, &1) === 0))
      |> generate(target, accumulator)

    if alternative_status === :error ||
         length(alternative_collection) >= length(obvious_collection),
       do: {:ok, obvious_collection},
       else: {:ok, alternative_collection}
  end
end
