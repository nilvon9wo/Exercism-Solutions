defmodule RotationalCipher do
  @doc """
  Given a plaintext and amount to shift by, return a rotated string.

  Example:
  iex> RotationalCipher.rotate("Attack at dawn", 13)
  "Nggnpx ng qnja"
  """
  @end_of_english_alphabet 26
  @start_of_ascii_uppercase_alphabet 65
  @end_of_ascii_uppercase_alphabet 90
  @start_of_ascii_lowercase_alphabet 97
  @end_of_ascii_lowercase_alphabet 122

  @spec rotate(text :: String.t(), shift :: integer) :: String.t()
  def rotate(text, shift),
    do:
      text
      |> to_charlist()
      |> Enum.map(&shift_character(&1, shift))
      |> to_string()

  def shift_character(character, shift)
      when shift >= @end_of_english_alphabet or
             (character <= @end_of_ascii_uppercase_alphabet and
                character + shift > @end_of_ascii_uppercase_alphabet) or
             character + shift > @end_of_ascii_lowercase_alphabet,
      do: character + shift - @end_of_english_alphabet

  def shift_character(character, _shift)
      when character < @start_of_ascii_uppercase_alphabet or
             (character > @end_of_ascii_uppercase_alphabet and
                character < @start_of_ascii_lowercase_alphabet) or
             character > @end_of_ascii_lowercase_alphabet,
      do: character

  def shift_character(character, shift),
    do: character + shift
end
