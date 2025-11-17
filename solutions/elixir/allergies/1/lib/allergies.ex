defmodule Allergies do
  @value_by_allergen %{
    eggs: 1,
    peanuts: 2,
    shellfish: 4,
    strawberries: 8,
    tomatoes: 16,
    chocolate: 32,
    pollen: 64,
    cats: 128
  }

  @allergens @value_by_allergen
             |> Enum.sort(&(elem(&1, 1) > elem(&2, 1)))
             |> Enum.map(&elem(&1, 0))
  @allergen_values Map.values(@value_by_allergen)
  @allergen_total Enum.sum(@allergen_values)
  @highest_assigned_allergen_code Enum.max(@allergen_values)

  @doc """
  List the allergies for which the corresponding flag bit is true.
  """
  @spec list(non_neg_integer) :: [String.t()]
  def list(flags)
      when flags > @allergen_total,
      do: list(flags - find_higher_power_of_two(flags, @allergen_total) - 1)

  defp find_higher_power_of_two(flags, power_of_two)
       when power_of_two > flags,
       do: power_of_two / 2

  defp find_higher_power_of_two(flags, power_of_two),
    do: find_higher_power_of_two(flags, power_of_two * 2)

  def list(flags),
    do: list(flags, _to_check_allergens = @allergens, possessed_allergies = [])

  defp list(0 = _flags, _to_check_allergens, possessed_allergies),
    do:
      possessed_allergies
      |> Enum.map(&Atom.to_string/1)

  defp list(flags, to_check_allergens, possessed_allergies)
       when flags <= @allergen_total,
       do: translate_code(flags, to_check_allergens, possessed_allergies)

  defp list(flags, to_check_allergens, possessed_allergies),
    do:
      flags
      |> ignore_extra_flags()
      |> list(to_check_allergens, possessed_allergies)

  defp translate_code(flags, [] = to_check_allergens, possessed_allergies),
    do: list(0, to_check_allergens, possessed_allergies)

  defp translate_code(flags, [first_allergen | remaining_allergens], possessed_allergies) do
    allergen_code = @value_by_allergen[first_allergen]

    if allergen_code <= flags,
      do:
        list(flags - allergen_code, remaining_allergens, [first_allergen | possessed_allergies]),
      else: list(flags, remaining_allergens, possessed_allergies)
  end

  defp ignore_extra_flags(flags),
    do: ignore_extra_flags(flags, highest_power_of_2(flags))

  defp ignore_extra_flags(flags, highest_code)
       when flags <= @allergen_total,
       do: flags

  defp ignore_extra_flags(flags, highest_code),
    do: ignore_extra_flags(flags - highest_code, highest_code / 2)

  defp highest_power_of_2(number, last_attempted \\ @highest_assigned_allergen_code) do
    next_attempt = last_attempted * 2

    if next_attempt > next_attempt,
      do: last_attempted,
      else: highest_power_of_2(number, next_attempt)
  end

  @doc """
  Returns whether the corresponding flag bit in 'flags' is set for the item.
  """
  @spec allergic_to?(non_neg_integer, String.t()) :: boolean
  def allergic_to?(flags, item),
    do:
      flags
      |> list()
      |> Enum.member?(item)
end
