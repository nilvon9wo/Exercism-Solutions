defmodule DndCharacter do
  @type t :: %__MODULE__{
          strength: pos_integer(),
          dexterity: pos_integer(),
          constitution: pos_integer(),
          intelligence: pos_integer(),
          wisdom: pos_integer(),
          charisma: pos_integer(),
          hitpoints: pos_integer()
        }
  @sides_on_die 6

  defstruct ~w[strength dexterity constitution intelligence wisdom charisma hitpoints]a

  @spec modifier(pos_integer()) :: integer()
  def modifier(score),
    do: Kernel.trunc(score / 2) - 5

  @spec ability :: pos_integer()
  def ability(),
    do:
      [roll_die(), roll_die(), roll_die(), roll_die()]
      |> drop_lowest()
      |> Enum.sum()

  defp roll_die(),
    do: :rand.uniform(@sides_on_die)

  defp drop_lowest(dice_values) do
    minimum_value = Enum.min(dice_values)
    minimum_index = Enum.find_index(dice_values, &(&1 === minimum_value))
    {_, new_dice_values} = List.pop_at(dice_values, minimum_index)

    new_dice_values
  end

  @spec character :: t()
  def character do
    constitution = ability()

    %DndCharacter{
      strength: ability(),
      dexterity: ability(),
      constitution: constitution,
      intelligence: ability(),
      wisdom: ability(),
      charisma: ability(),
      hitpoints: 10 + modifier(constitution)
    }
  end
end
