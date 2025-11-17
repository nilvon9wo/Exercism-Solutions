defmodule Raindrops do
  @sound_by_factor %{
    3 => "Pling",
    5 => "Plang",
    7 => "Plong"
  }
  @no_sound ""
  @is_factor true
  @not_factor false

  @doc """
  Returns a string based on raindrop factors.

  - If the number contains 3 as a prime factor, output 'Pling'.
  - If the number contains 5 as a prime factor, output 'Plang'.
  - If the number contains 7 as a prime factor, output 'Plong'.
  - If the number does not contain 3, 5, or 7 as a prime factor,
    just pass the number's digits straight through.
  """
  @spec convert(pos_integer) :: String.t()
  def convert(number) do
    sounds = convert(number, @sound_by_factor)

    if sounds !== "",
      do: sounds,
      else: to_string(number)
  end

  defp convert(number, %{} = sound_by_factor),
    do:
      sound_by_factor
      |> Enum.map(&convert(number, &1))
      |> Enum.reject(&(&1 === @no_sound))
      |> Enum.join("")

  defp convert(number, {value, sound}),
    do: make_sound(is_factor?(number, value), sound)

  defp make_sound(@is_factor, sound),
    do: sound

  defp make_sound(@not_factor, sound),
    do: @no_sound

  defp is_factor?(number, value),
    do: rem(number, value) === 0
end
